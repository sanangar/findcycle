package solution.impl.client;

import solution.api.client.Client;
import solution.api.client.ClientManager;
import solution.api.resource.Resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientManagerImpl implements ClientManager {

    private final ConcurrentMap<String, ClientImpl> clients = new ConcurrentHashMap<String, ClientImpl>();

    public Client getClientById(String id) {
        return getClient(id);
    }

    public ClientImpl getClient(String id) {
        if ( id == null ) {
            throw new NullPointerException("Client id is null.");
        }
        return clients.get(id);
    }

    synchronized public ClientImpl createClient(String id, Map<Resource, Long> balance) {
        if ( clients.containsKey(id) ) {
            return null;
        }
        ClientImpl client = new ClientImpl(id, balance);
        clients.put(id, client);
        return client;
    }
}
