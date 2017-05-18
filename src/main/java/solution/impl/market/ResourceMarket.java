package solution.impl.market;

import solution.api.client.Client;
import solution.api.order.Order;
import solution.api.resource.Resource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceMarket {

    public final MarketEngine engine;
    public final Resource resource;
    public final Resource priceIn;
    public Map<String, Map<Order.Type, Deque<Order>>> transactionCondition2Type2deque = new ConcurrentHashMap<String, Map<Order.Type, Deque<Order>>>();

    public ResourceMarket(MarketEngine marketEngine, Resource resource, Resource priceResource) {
        engine = marketEngine;
        this.resource = resource;
        this.priceIn = priceResource;
    }

    public boolean startOrder(Order order) {
        Map<Order.Type, Deque<Order>> dequeMap = getType2DequeOrders(order);
        Client client = order.getClient();
        Order.Type type = order.getType();
        Order.Type contrType = type == Order.Type.BUY ? Order.Type.SALE : Order.Type.BUY;
        synchronized (dequeMap) {
            Deque<Order> orders = dequeMap.get(contrType);
            for ( Order o : orders ) {
                if ( !o.getClient().equals(client) ) {
                    execute(order, o);
                    orders.remove(o);
                    return true;
                }
            }
            dequeMap.get(type).addLast(order);
            return false;
        }
    }

    private void execute(Order o1, Order o2) {
        boolean log = o1.getResource().getId().equals("B") && o1.getPrice() == 5 && o1.getNumber() == 3;
        //why tree map : because i can to add lock by every client before starting, but i should lock them in specified order to avoid deadlocks
        Map<String, Order> clientId2Order = new TreeMap<String, Order>();
        clientId2Order.put(o1.getClient().getId(), o1);
        clientId2Order.put(o2.getClient().getId(), o2);
        for (Map.Entry<String, Order> e : clientId2Order.entrySet()) {
            engine.clientManager.getClient(e.getKey()).handleOrder(e.getValue(), priceIn);
        }
    }

    private Map<Order.Type, Deque<Order>> getType2DequeOrders(Order order) {
        String key = generateTransactionConditionKey(order);
        if ( !transactionCondition2Type2deque.containsKey(key) ) {
            Map<Order.Type, Deque<Order>> dequeMap = new HashMap<Order.Type, Deque<Order>>(2);
            dequeMap.put(Order.Type.BUY, new LinkedList<Order>());
            dequeMap.put(Order.Type.SALE, new LinkedList<Order>());
            transactionCondition2Type2deque.putIfAbsent(key, dequeMap);
        }
        return transactionCondition2Type2deque.get(key);
    }

    private String generateTransactionConditionKey(Order order) {
        return order.getNumber() + "_" + order.getPrice();
    }

}
