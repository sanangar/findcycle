package solution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import solution.api.market.Market;
import solution.api.resource.Resource;
import solution.impl.client.ClientManagerImpl;
import solution.impl.market.MarketEngine;
import solution.impl.market.MarketImpl;
import solution.impl.order.OrderManagerImpl;
import solution.impl.resource.ResourceManagerImpl;

import java.util.HashMap;

public class ClientManagerTest {

    private Market market;
    private Resource dollarResource;

    private static final String CLIENT_NAME = "C1";

    @Before
    public void before() {
        MarketEngine engine = new MarketEngine();
        engine.resourceManager = new ResourceManagerImpl();
        dollarResource = engine.resourceManager.getResourceById("$");
        engine.priceIn = dollarResource;
        engine.clientManager = new ClientManagerImpl();
        engine.clientManager.createClient(CLIENT_NAME, new HashMap<Resource, Long>());
        engine.orderManager = new OrderManagerImpl();
        market = new MarketImpl(engine);
    }

    @Test(expected=NullPointerException.class)
    public void testNPE() {
        market.getClientManager().getClientById(null);
    }

    @Test()
    public void testClientName() {
        Assert.assertTrue(market.getClientManager().getClientById(CLIENT_NAME).getId().equals(CLIENT_NAME));
    }

    @Test(expected=NullPointerException.class)
    public void testNPEResource() {
        market.getClientManager().getClientById(CLIENT_NAME).getResourceNumber(null);
    }

    @Test()
    public void testClientBalance() {
        Assert.assertTrue(market.getClientManager().getClientById(CLIENT_NAME).getResourceNumber(dollarResource) == 0);
    }

}