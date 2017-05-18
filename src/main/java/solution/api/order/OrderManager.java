package solution.api.order;

import solution.api.client.Client;
import solution.api.resource.Resource;

import java.util.Map;

public interface OrderManager {

    public Order createBuyOrder(Client client, Resource resource, long resourceNumber, long price);

    public Order createSaleOrder(Client client, Resource resource, long resourceNumber, long price);

}
