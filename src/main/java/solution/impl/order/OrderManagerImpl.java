package solution.impl.order;

import solution.api.client.Client;
import solution.api.order.Order;
import solution.api.order.OrderManager;
import solution.api.resource.Resource;

public class OrderManagerImpl implements OrderManager {

    public Order createBuyOrder(Client client, Resource resource, long resourceNumber, long price) {
        return createOrder(Order.Type.BUY, client, resource, resourceNumber, price);
    }

    public Order createSaleOrder(Client client, Resource resource, long resourceNumber, long price) {
        return createOrder(Order.Type.SALE, client, resource, resourceNumber, price);
    }

    private Order createOrder(Order.Type type, Client client, Resource resource, long resourceNumber, long price) {
        if ( client == null ) {
            throw new NullPointerException("Client is null.");
        }
        if ( resource == null ) {
            throw new NullPointerException("Resource is null.");
        }
        if ( resourceNumber <= 0 ) {
            throw new IllegalArgumentException("Resource number should be positive.");
        }
        if ( price <= 0 ) {
            throw new IllegalArgumentException("Price number should be positive.");
        }
        return new OrderImpl(type, client, resource, resourceNumber, price);
    }
}
