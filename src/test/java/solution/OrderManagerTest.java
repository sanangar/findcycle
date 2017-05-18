package solution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import solution.api.client.Client;
import solution.api.market.Market;
import solution.api.order.Order;
import solution.api.resource.Resource;
import solution.impl.client.ClientManagerImpl;
import solution.impl.market.MarketEngine;
import solution.impl.market.MarketImpl;
import solution.impl.order.OrderManagerImpl;
import solution.impl.resource.ResourceManagerImpl;

import java.util.HashMap;

public class OrderManagerTest {

    private Market market;
    private Resource dollarResource;
    private Resource someResource;
    private Client client;

    @Before
    public void before() {
        MarketEngine engine = new MarketEngine();
        engine.resourceManager = new ResourceManagerImpl();
        dollarResource = engine.resourceManager.getResourceById("$");
        someResource = engine.resourceManager.getResourceById("some");
        engine.priceIn = dollarResource;
        engine.clientManager = new ClientManagerImpl();
        client = engine.clientManager.createClient("client", new HashMap<Resource, Long>());
        engine.orderManager = new OrderManagerImpl();
        market = new MarketImpl(engine);
    }

    @Test(expected=NullPointerException.class)
    public void testNPEClient() {
        market.getOrderManager().createBuyOrder(null, someResource, 1, 1);
    }

    @Test(expected=NullPointerException.class)
    public void testNPEResource() {
        market.getOrderManager().createBuyOrder(client, null, 1, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZeroNumber() {
        market.getOrderManager().createBuyOrder(client, someResource, 0, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZeroPrice() {
        market.getOrderManager().createBuyOrder(client, someResource, 1, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNegativeNumber() {
        market.getOrderManager().createBuyOrder(client, someResource, -1, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNegativePrice() {
        market.getOrderManager().createBuyOrder(client, someResource, 1, -1);
    }

    @Test
    public void testBuyOrder() {
        Order order = market.getOrderManager().createBuyOrder(client, someResource, 2, 15);
        Assert.assertTrue(order.getType() == Order.Type.BUY);
        Assert.assertTrue(order.getClient().getId().equals(client.getId()));
        Assert.assertTrue(order.getResource().getId().equals(someResource.getId()));
        Assert.assertTrue(order.getNumber() == 2);
        Assert.assertTrue(order.getPrice() == 15);
    }

    @Test
    public void testSaleOrder() {
        Order order = market.getOrderManager().createSaleOrder(client, someResource, 2, 15);
        Assert.assertTrue(order.getType() == Order.Type.SALE);
        Assert.assertTrue(order.getClient().getId().equals(client.getId()));
        Assert.assertTrue(order.getResource().getId().equals(someResource.getId()));
        Assert.assertTrue(order.getNumber() == 2);
        Assert.assertTrue(order.getPrice() == 15);
    }

}
