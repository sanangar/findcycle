package solution.impl.order;

import solution.api.client.Client;
import solution.api.order.Order;
import solution.api.resource.Resource;

public class OrderImpl implements Order {

    private final Type type;
    private final Client client;
    private final Resource resource;
    private final long number;
    private final long price;

    public OrderImpl(Type type, Client client, Resource resource, long number, long price) {
        this.type = type;
        this.client = client;
        this.resource = resource;
        this.number = number;
        this.price = price;
    }


    public Type getType() {
        return type;
    }

    public Client getClient() {
        return client;
    }

    public Resource getResource() {
        return resource;
    }

    public long getNumber() {
        return number;
    }

    public long getPrice() {
        return price;
    }

}
