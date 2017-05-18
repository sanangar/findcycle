package solution.api.client;

import solution.api.order.Order;
import solution.api.resource.Resource;

public interface Client {

    public String getId();

    public long getResourceNumber(Resource resource);

}
