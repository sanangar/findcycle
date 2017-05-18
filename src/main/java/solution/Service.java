package solution;

import solution.api.client.Client;
import solution.api.market.Market;
import solution.api.order.Order;
import solution.api.resource.Resource;
import solution.api.resource.ResourceManager;
import solution.impl.client.ClientManagerImpl;
import solution.impl.market.MarketEngine;
import solution.impl.market.MarketImpl;
import solution.impl.order.OrderManagerImpl;
import solution.impl.resource.ResourceManagerImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Service {

    private final ClientManagerImpl clientManager;
    private final Market market;
    private final Resource[] resources;

    public Service(String ... resourceNames) {
        ResourceManager resourceManager = new ResourceManagerImpl();
        resources = new Resource[resourceNames.length];
        for ( int i=0; i<resourceNames.length; i++ ) {
            resources[i] = resourceManager.getResourceById(resourceNames[i]);
        }

        MarketEngine engine = new MarketEngine();
        engine.priceIn = resources[0];
        engine.resourceManager = resourceManager;
        engine.clientManager = new ClientManagerImpl();
        clientManager = engine.clientManager;

        engine.orderManager = new OrderManagerImpl();
        market = new MarketImpl(engine);
    }

    public static void main(String ... args) throws Exception {
        String[] resources = new String[]{"$", "A", "B", "C", "D"};
        Service service = new Service(resources);

        BufferedReader clientReader = getBufferedReader("clients.txt");
        while (clientReader.ready()) {
            String s = clientReader.readLine();
            String[] line = s.split("\\s");
            if ( line.length < 2 ) {
                System.out.println("Illegal format of client.txt : '"+s+"'");
                continue;
            }
            String clientId = line[0];
            List<Long> balance = new ArrayList<Long>(line.length - 1);
            for ( int i=1; i<line.length; i++ ) {
                balance.add(Long.parseLong(line[i]));
            }
            long[] b = new long[balance.size()];
            for ( int i=0; i<balance.size(); i++ ) {
                b[i] = balance.get(i);
            }
            service.addClient(clientId, b);
        }
        clientReader.close();

        BufferedReader orderReader = getBufferedReader("orders.txt");
        while (orderReader.ready()) {
            String s = orderReader.readLine();
            String[] line = s.split("\\s");
            if ( line.length != 5 ) {
                System.out.println("Illegal format of orders.txt : '"+s+"'");
                continue;
            }
            if ( "b".equals(line[1]) ) {
                service.addBuyOrder(line[0], line[2], Long.parseLong(line[3]), Long.parseLong(line[4]));
            } else if ( "s".equals(line[1]) ) {
                service.addSaleOrder(line[0], line[2], Long.parseLong(line[3]), Long.parseLong(line[4]));
            } else {
                System.out.println("Illegal format of orders.txt : '"+s+"'");
                continue;
            }
        }
        orderReader.close();

        for ( int i=1; i<10; i++ ) {
            String client = "C"+i;
            StringBuilder sb = new StringBuilder(client);
            for ( String r : resources ) {
                sb.append("\t").append(service.getClientBalanceByResource(client, r));
            }
            System.out.println(sb.toString());
        }
    }

    public long getClientBalanceByResource(String client, String resource) {
        return market.getClientManager().getClientById(client).getResourceNumber(
                market.getResourceManager().getResourceById(resource)
        );
    }

    public void addClient(String id, long ... balance) {
        int num = Math.min(resources.length, balance.length);
        Map<Resource, Long> balanceMap = new HashMap<Resource, Long>(num);
        for ( int i=0; i<num; i++ ) {
            balanceMap.put(resources[i], balance[i]);
        }
        clientManager.createClient(id, balanceMap);
    }

    public void addBuyOrder(String id, String resourceName, long price, long resourceNumber) {
        Resource resource = market.getResourceManager().getResourceById(resourceName);
        Client client = market.getClientManager().getClientById(id);
        Order order = market.getOrderManager().createBuyOrder(client, resource, resourceNumber, price);
        market.startOrder(order);
    }

    public void addSaleOrder(String id, String resourceName, long price, long resourceNumber) {
        Resource resource = market.getResourceManager().getResourceById(resourceName);
        Client client = market.getClientManager().getClientById(id);
        Order order = market.getOrderManager().createSaleOrder(client, resource, resourceNumber, price);
        market.startOrder(order);
    }

    private static BufferedReader getBufferedReader(String resourceName) {
        return new BufferedReader(new InputStreamReader(Service.class.getClassLoader().getResourceAsStream(resourceName)));
    }

}
