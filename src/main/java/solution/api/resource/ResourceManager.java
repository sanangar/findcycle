package solution.api.resource;

import java.util.Collection;
import java.util.Set;

public interface ResourceManager {

    public Resource getResourceById(String id);

    public Collection<Resource> getResources();

}
