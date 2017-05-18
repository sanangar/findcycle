package solution.impl.resource;

import solution.api.resource.Resource;

public class ResourceImpl implements Resource {
    private final String id;

    public ResourceImpl(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
