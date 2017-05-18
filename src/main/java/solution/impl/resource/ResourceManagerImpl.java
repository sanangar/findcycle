package solution.impl.resource;

import solution.api.resource.Resource;
import solution.api.resource.ResourceManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ResourceManagerImpl implements ResourceManager {

    private final ConcurrentMap<String, Resource> resources = new ConcurrentHashMap<String, Resource>();

    public Resource getResourceById(String id) {
        if ( id == null ) {
            throw new NullPointerException("Resource id is null.");
        }
        Resource resource = resources.get(id);
        if ( resource != null ) {
            return resource;
        }
        resources.putIfAbsent(id, new ResourceImpl(id));
        return resources.get(id);
    }

    public Collection<Resource> getResources() {
        return resources.values();
    }
}
