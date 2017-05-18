package solution.api.market;

import solution.api.client.ClientManager;
import solution.api.order.Order;
import solution.api.order.OrderManager;
import solution.api.resource.ResourceManager;

public interface Market {

    public ClientManager getClientManager();

    public ResourceManager getResourceManager();

    public OrderManager getOrderManager();

    public boolean startOrder(Order order);

}
