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
import java.util.Map;

/**
 * Created by dubov on 18.05.2017.
 */
public class MarketTest {

    private Market market;
    private Resource dollarResource;
    private Resource someResource;
    private Client seller;
    private Client buyer;

    @Before
    public void before() {
        MarketEngine engine = new MarketEngine();
        engine.resourceManager = new ResourceManagerImpl();
        dollarResource = engine.resourceManager.getResourceById("$");
        someResource = engine.resourceManager.getResourceById("some");
        engine.priceIn = dollarResource;
        engine.clientManager = new ClientManagerImpl();
        Map<Resource, Long> balance = new HashMap<Resource, Long>(2);
        balance.put(dollarResource, 1000L);
        balance.put(someResource, 100L);
        seller = engine.clientManager.createClient("seller", balance);
        buyer = engine.clientManager.createClient("buyer", balance);
        engine.orderManager = new OrderManagerImpl();
        market = new MarketImpl(engine);
    }

    @Test(expected=NullPointerException.class)
    public void testNPEOrder() {
        market.startOrder(null);
    }

    @Test
    public void testNPEResource() {
        Order buyerBuy = market.getOrderManager().createBuyOrder(buyer, someResource, 2, 15);
        Order buyerSale = market.getOrderManager().createSaleOrder(buyer, someResource, 2, 15);
        market.startOrder(buyerBuy);
        market.startOrder(buyerSale);
        Assert.assertTrue(buyer.getResourceNumber(dollarResource) == 1000);
        Assert.assertTrue(seller.getResourceNumber(dollarResource) == 1000);
        Assert.assertTrue(buyer.getResourceNumber(someResource) == 100);
        Assert.assertTrue(seller.getResourceNumber(someResource) == 100);

        Order sellerSaleUpPrice = market.getOrderManager().createSaleOrder(seller, someResource, 2, 5);
        market.startOrder(sellerSaleUpPrice);
        Assert.assertTrue(buyer.getResourceNumber(dollarResource) == 1000);
        Assert.assertTrue(seller.getResourceNumber(dollarResource) == 1000);
        Assert.assertTrue(buyer.getResourceNumber(someResource) == 100);
        Assert.assertTrue(seller.getResourceNumber(someResource) == 100);

        Order sellerSale = market.getOrderManager().createSaleOrder(seller, someResource, 2, 15);
        market.startOrder(sellerSale);
        Assert.assertTrue(buyer.getResourceNumber(dollarResource) == 970);
        Assert.assertTrue(seller.getResourceNumber(dollarResource) == 1030);
        Assert.assertTrue(buyer.getResourceNumber(someResource) == 102);
        Assert.assertTrue(seller.getResourceNumber(someResource) == 98);
    }
}
