package solution.impl.market;

import solution.api.order.Order;
import solution.api.order.OrderManager;
import solution.api.resource.Resource;
import solution.api.resource.ResourceManager;
import solution.impl.client.ClientManagerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MarketEngine {

    private final Map<Resource, ResourceMarket> resourceMarkets = new ConcurrentHashMap<Resource, ResourceMarket>();

    public ClientManagerImpl clientManager;
    public ResourceManager resourceManager;
    public OrderManager orderManager;
    public Resource priceIn;

    public boolean startOrder(Order order) {
        if ( order == null ) {
            throw new NullPointerException("order is null");
        }
        ResourceMarket resourceMarket = getResourceMarket(order.getResource());
        return resourceMarket.startOrder(order);
    }

    private ResourceMarket getResourceMarket(Resource resource) {
        if (!resourceMarkets.containsKey(resource)) {
            resourceMarkets.putIfAbsent(resource, new ResourceMarket(this, resource, priceIn));
        }
        return resourceMarkets.get(resource);
    }
}
