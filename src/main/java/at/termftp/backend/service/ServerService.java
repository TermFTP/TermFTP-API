package at.termftp.backend.service;

import at.termftp.backend.dao.ServerGroupRepository;
import at.termftp.backend.dao.ServerGroupServerRepository;
import at.termftp.backend.dao.ServerRepository;
import at.termftp.backend.model.Server;
import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.ServerGroupServer;

import java.util.List;
import java.util.UUID;

public class ServerService {
    private final ServerRepository serverRepository;
    private final ServerGroupRepository serverGroupRepository;
    private final ServerGroupServerRepository serverGroupServerRepository;

    public ServerService(ServerRepository serverRepository, ServerGroupRepository serverGroupRepository, ServerGroupServerRepository serverGroupServerRepository) {
        this.serverRepository = serverRepository;
        this.serverGroupRepository = serverGroupRepository;
        this.serverGroupServerRepository = serverGroupServerRepository;
    }

    public Server getServerById(UUID serverID){
        return serverRepository.findServerByServerID(serverID).orElse(null);
    }

    public Server createServer(Server server){
        return serverRepository.save(server);
    }

    public void addServersToServerGroup(List<Server> servers, ServerGroup serverGroup){
        for(Server server : servers){
            serverGroupServerRepository.save(new ServerGroupServer(server.getServerID(),
                    serverGroup.getGroupID(),
                    serverGroup.getUserID()));
        }
    }



    public ServerGroup createServerGroup(ServerGroup serverGroup){
        return serverGroupRepository.save(serverGroup);
    }

    public ServerGroup getServerGroupByID(UUID serverGroupID){
        return serverGroupRepository.findServerGroupByGroupID(serverGroupID).orElse(null);
    }

    public List<ServerGroup> getAllServerGroupsForUser(UUID userID){
        return serverGroupRepository.findServerGroupsByUserID(userID).orElse(null);
    }

    public ServerGroup getAllServerGroupsForUserByName(UUID userID, String name){
        return serverGroupRepository.findServerGroupByUserIDAndName(userID, name).orElse(null);
    }


}
