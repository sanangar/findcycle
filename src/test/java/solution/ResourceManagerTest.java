package solution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import solution.api.market.Market;
import solution.impl.client.ClientManagerImpl;
import solution.impl.market.MarketEngine;
import solution.impl.market.MarketImpl;
import solution.impl.order.OrderManagerImpl;
import solution.impl.resource.ResourceManagerImpl;

public class ResourceManagerTest {

    private Market market;

    @Before
    public void before() {
        MarketEngine engine = new MarketEngine();
        engine.resourceManager = new ResourceManagerImpl();
        engine.priceIn = engine.resourceManager.getResourceById("$");
        engine.clientManager = new ClientManagerImpl();
        engine.orderManager = new OrderManagerImpl();
        market = new MarketImpl(engine);
    }

    @Test(expected=NullPointerException.class)
    public void testNPE() {
        market.getResourceManager().getResourceById(null);
    }

    @Test()
    public void testResourceName() {
        String resourceName = "resourceName";
        Assert.assertTrue(market.getResourceManager().getResourceById(resourceName).getId().equals(resourceName));
    }

}
