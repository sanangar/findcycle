package solution.impl.client;

import solution.api.client.Client;
import solution.api.order.Order;
import solution.api.resource.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientImpl implements Client {
    private final String id;
    private final Map<Resource, Long> balance;

    public ClientImpl(String id, Map<Resource, Long> balance) {
        this.id = id;
        this.balance = new ConcurrentHashMap<Resource, Long>(balance != null ? balance : new HashMap<Resource, Long>());
    }

    public String getId() {
        return id;
    }

    public long getResourceNumber(Resource resource) {
        if ( resource == null ) {
            throw new NullPointerException("Resource is null.");
        }
        Long value =  balance.get(resource);
        return value == null ? 0 : value;
    }

    synchronized public void handleOrder(Order order, Resource priceIn) {
        Resource r1 = order.getResource();
        long v1 = getResourceNumber(r1);
        Resource r2 = priceIn;
        long v2 = getResourceNumber(r2);
        if ( order.getType() == Order.Type.BUY ) {
            v1 = v1 + order.getNumber();
            v2 = v2 - order.getNumber() * order.getPrice();
        } else {
            v1 = v1 - order.getNumber();
            v2 = v2 + order.getNumber() * order.getPrice();
        }
        balance.put(r1, v1);
        balance.put(r2, v2);
    }
}
