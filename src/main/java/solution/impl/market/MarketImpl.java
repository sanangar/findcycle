package solution.impl.market;

import solution.api.client.ClientManager;
import solution.api.market.Market;
import solution.api.order.Order;
import solution.api.order.OrderManager;
import solution.api.resource.ResourceManager;

public class MarketImpl implements Market {

    private final MarketEngine engine;

    public MarketImpl(MarketEngine engine) {
        this.engine = engine;
    }

    public ClientManager getClientManager() {
        return engine.clientManager;
    }

    public ResourceManager getResourceManager() {
        return engine.resourceManager;
    }

    public OrderManager getOrderManager() {
        return engine.orderManager;
    }

    public boolean startOrder(Order order) {
        return engine.startOrder(order);
    }
}
