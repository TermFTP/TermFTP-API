package at.termftp.backend.service;

import at.termftp.backend.dao.ServerGroupRepository;
//import at.termftp.backend.dao.ServerGroupServerRepository;
import at.termftp.backend.dao.ServerGroupServerRepository;
import at.termftp.backend.dao.ServerRepository;
import at.termftp.backend.model.Server;
import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.ServerGroupServer;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        System.out.println("creating server " + server.getName());
        return serverRepository.save(server);
    }



    public void updateServer(Server server){
        server = serverRepository.save(server);
        System.out.println("> Updated Server");
    }


    public boolean removeServerFromServerGroup(UUID serverID, ServerGroup serverGroup){
        ServerGroupServer serverGroupServer = serverGroup.getServerGroupServers()
                .stream()
                .filter(sgs -> sgs.getServer().getServerID().equals(serverID))
                .findFirst().orElse(null);

        if(serverGroupServer != null){
            serverGroupServerRepository.delete(serverGroupServer);
            System.out.println(">> removed 1 server from serverGroup");
        }

        return serverGroupServer != null;
    }


    public boolean removeServerGroup(User user, UUID groupID){

        List<ServerGroup> serverGroups = getAllServerGroupsForUser(user.getUserID());
        ServerGroup serverGroup = serverGroups.stream()
                .filter(sg -> sg.getGroupID().equals(groupID))
                .findFirst().orElse(null);

        if(serverGroup != null){
            serverGroupRepository.delete(serverGroup);
            System.out.println(">> removed 1 serverGroup");
        }

        return serverGroup != null;

    }

    public void removeServerGroupServer(List<ServerGroup> serverGroups, UUID serverID){
        for(ServerGroup serverGroup : serverGroups){
            removeServerFromServerGroup(serverID, serverGroup);

            if(serverGroup.getServerGroups().size() > 0){
                removeServerGroupServer(new ArrayList<>(serverGroup.getServerGroups()), serverID);
            }
        }
    }

    public boolean removeServer(User user, UUID serverID){
        Server server = serverRepository.findServerByServerID(serverID).orElse(null);
        if(server != null){

            List<ServerGroup> serverGroups = getAllServerGroupsForUser(user.getUserID());
            removeServerGroupServer(serverGroups, serverID);

            serverRepository.delete(server);
            System.out.println(">> removed 1 Server.");
        }
        return server != null;
    }




    public ServerGroup getServerGroupByID(UUID groupID, UUID userID){
        return serverGroupRepository.findServerGroupByID(groupID, userID).orElse(null);
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
        System.out.println("> added " + servers.size() + " server(s) to ServerGroup " + serverGroup.getName());
        return serverGroup;
    }

    /**
     * used to add a single Server to an existing ServerGroup
     * @param server Server
     * @param serverGroup ServerGroup
     */
    public void addServerToServerGroup(Server server, ServerGroup serverGroup){

        ServerGroupServer sgs = new ServerGroupServer(server, serverGroup);
        sgs = serverGroupServerRepository.save(sgs);
        serverGroup.getServerGroupServers().add(sgs);

        System.out.println("> added Server " + server.getName() + "to ServerGroup " + serverGroup.getName());
    }


    /**
     * used to create/change a ServerGroup
     * @param serverGroup the ServerGroup that should be created/changed
     * @return the new ServerGroup
     */
    public ServerGroup saveServerGroup(ServerGroup serverGroup){
        System.out.println("> saving ServerGroup: " + serverGroup.getName());
        return serverGroupRepository.save(serverGroup);
    }


    public void updateChildGroups(ServerGroup serverGroup) throws IllegalArgumentException{
        for(ServerGroup childGroup : serverGroup.getServerGroups()){
            if(childGroup == null){
                throw new IllegalArgumentException("Child-Group does not exist!");
            }
            if(childGroup.getGroupID().equals(serverGroup.getGroupID())){
                throw new IllegalArgumentException("Child-Group cannot be its own parent");
            }
            serverGroupRepository.addParentServerGroup(childGroup.getGroupID(), childGroup.getUserID(), serverGroup.getGroupID());
        }
        System.out.println("> updated ServerGroup " + serverGroup.getName() + " (updated Child-Groups)");
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
