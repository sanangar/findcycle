package solution.api.order;

import solution.api.client.Client;
import solution.api.resource.Resource;

import java.util.Map;
import java.util.Set;

public interface Order {

    public enum Type{BUY, SALE}

    public Type getType();

    public Client getClient();

    public Resource getResource();

    public long getNumber();

    public long getPrice();


}
