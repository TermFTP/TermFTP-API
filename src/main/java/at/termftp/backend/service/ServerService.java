package at.termftp.backend.service;

import at.termftp.backend.dao.ServerGroupRepository;
//import at.termftp.backend.dao.ServerGroupServerRepository;
import at.termftp.backend.dao.ServerGroupServerRepository;
import at.termftp.backend.dao.ServerRepository;
import at.termftp.backend.model.Server;
import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.ServerGroupServer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServerService {
    private final ServerRepository serverRepository;
    private final ServerGroupRepository serverGroupRepository;
    private final ServerGroupServerRepository serverGroupServerRepository;

    public ServerService(ServerRepository serverRepository, ServerGroupRepository serverGroupRepository, ServerGroupServerRepository serverGroupServerRepository) {
        this.serverRepository = serverRepository;
        this.serverGroupRepository = serverGroupRepository;
        this.serverGroupServerRepository = serverGroupServerRepository;
    }

    /**
     * used to get a Server by its ID
     * @param serverID ID of the server
     * @return Server
     */
    public Server getServerById(UUID serverID){
        return serverRepository.findServerByServerID(serverID).orElse(null);
    }

    /**
     * used to create a Server (or save/change a server)
     * @param server the Server
     * @return Server
     */
    public Server createServer(Server server){
        return serverRepository.save(server);
    }


    /**
     * used to add a list of Servers to an existing ServerGroup
     * @param servers {@code List<Server>}
     * @param serverGroup ServerGroup
     * @return ServerGroup
     */
    public ServerGroup addServersToServerGroup(List<Server> servers, ServerGroup serverGroup){
        for(Server server : servers){
            ServerGroupServer sgs = new ServerGroupServer(server, serverGroup);
            sgs = serverGroupServerRepository.save(sgs);
            serverGroup.getServerGroupServers().add(sgs);
        }

        return serverGroup;
    }

    /**
     * used to add a single Server to an existing ServerGroup
     * @param server Server
     * @param serverGroup ServerGroup
     * @return ServerGroup
     */
    public ServerGroup addServerToServerGroup(Server server, ServerGroup serverGroup){

        ServerGroupServer sgs = new ServerGroupServer(server, serverGroup);
        sgs = serverGroupServerRepository.save(sgs);
        serverGroup.getServerGroupServers().add(sgs);

        return serverGroup;
    }


    /**
     * used to create/change a ServerGroup
     * @param serverGroup the ServerGroup that should be created/changed
     * @return the new ServerGroup
     */
    public ServerGroup saveServerGroup(ServerGroup serverGroup){
        return serverGroupRepository.save(serverGroup);
    }

    /**
     * used to get a ServerGroup by its ID
     * @param serverGroupID UUID
     * @return ServerGroup
     */
    public ServerGroup getServerGroupByID(UUID serverGroupID){
        return serverGroupRepository.findServerGroupByGroupID(serverGroupID).orElse(null);
    }

    /**
     * used to get all ServerGroups that are mapped to a user
     * @param userID the User's ID
     * @return a List of ServerGroups
     */
    public List<ServerGroup> getAllServerGroupsForUser(UUID userID){
        return serverGroupRepository.findServerGroupsByUserID(userID).orElse(null);
    }

    /**
     * used to get a single ServerGroup by its name (and user)
     * @param userID the User's ID
     * @param name the name of the ServerGroup
     * @return ServerGroup
     */
    public ServerGroup getServerGroupForUserByName(UUID userID, String name){
        return serverGroupRepository.findServerGroupByUserIDAndName(userID, name).orElse(null);
    }


}
