package at.termftp.backend.api;

import at.termftp.backend.model.*;
import at.termftp.backend.service.AccessTokenService;
import at.termftp.backend.service.HistoryItemService;
import at.termftp.backend.service.ServerService;
import at.termftp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.PUT})
public class ServerController {

    private final ServerService serverService;
    private final AccessTokenService accessTokenService;
    private final UserService userService;
    private final HistoryItemService historyItemService;

    @Autowired
    public ServerController(ServerService serverService, AccessTokenService accessTokenService, UserService userService, HistoryItemService historyItemService) {
        this.serverService = serverService;
        this.accessTokenService = accessTokenService;
        this.userService = userService;
        this.historyItemService = historyItemService;
    }


    /**
     * wrapper used for grouping Servers
     * @param group Group
     * @param serverGroup ServerGroup
     * @return ServerGroup
     * @throws IllegalArgumentException Invalid Server-IDs
     */
    private Object groupServers(Group group, ServerGroup serverGroup, UUID userID) throws IllegalArgumentException{
        // add existing servers to group
        if(group.getServers() != null && group.getServers().size() > 0){
            List<UUID> serverIDs = group.getServers().stream().map(UUID::fromString).collect(Collectors.toList());
            List<Server> servers = serverIDs.stream().map(serverService::getServerById).collect(Collectors.toList());
            serverGroup = serverService.addServersToServerGroup(servers, serverGroup);
        }

        // add ServerGroups (children) to a Parent-ServerGroup
        if(group.getGroups() != null){
            List<UUID> groupIDs = group.getGroups().stream().map(UUID::fromString).collect(Collectors.toList());
            List<ServerGroup> serverGroups = groupIDs.stream().map(groupID -> serverService.getServerGroupByID(groupID, userID)).collect(Collectors.toList());

            serverGroup.getServerGroups().addAll(serverGroups);
            serverService.updateChildGroups(serverGroup);
        }




        return DefaultResponse.createResponse(serverGroup, "Server-Group");
    }


    /**
     * used to create Groups of servers
     * @param group Group(groupID, name, servers)
     * @param token users' AccessToken as String
     * @return ServerGroup
     */
    @PostMapping(path = "/group")
    public Object grouping(@RequestBody Group group,
                           @RequestHeader("Access-Token") String token){
        // validate AccessToken
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        // do grouping
        try{
            if(group.getGroupID() == null){
                // new empty group
                ServerGroup serverGroup = new ServerGroup(group.getName(), user);
                serverGroup = serverService.saveServerGroup(serverGroup);
                return groupServers(group, serverGroup, user.getUserID());

            }else{
                UUID groupID = UUID.fromString(group.getGroupID());
                ServerGroup serverGroup = serverService.getServerGroupByID(groupID);

                // change the name of the group, if required
                if(group.getName() != null){
                    serverGroup.setName(group.getName());

                    serverGroup = serverService.saveServerGroup(serverGroup);
                    System.out.println(serverGroup);
                }
                return groupServers(group, serverGroup, user.getUserID());
            }
        }catch(IllegalArgumentException e){
            String message = e.getMessage().contains("Child-Group") ? "; " + e.getMessage() : "";
            return ResponseEntity.status(400)
                    .body(new DefaultResponse(400, "Bad Request", "Invalid Server-IDs (must be of type UUID!)" + message));
        }
    }




    /**
     * used to create a single Server. This server will be automatically
     * added to the 'default'-ServerGroup.
     * @param server the Server
     * @param token users' AccessToken as String
     * @return Server
     */
    @PostMapping(path = "/createServer")
    public Object createServer(@RequestBody Server server,
                               @RequestHeader("Access-Token") String token){

        // validate AT
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        ServerGroup serverGroup = serverService.getServerGroupForUserByName(user.getUserID(), "default");
        server = serverService.createServer(server);
        serverService.addServerToServerGroup(server, serverGroup);

        return DefaultResponse.createResponse(server, "Created Server");
    }


    @PutMapping(path = "/updateServer")
    public Object updateServer(@RequestBody Server server,
                               @RequestHeader("Access-Token") String token){
        // validate AT
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        if(serverService.getServerById(server.getServerID()) == null){
            return ResponseEntity.status(400)
                    .body(new DefaultResponse(400, "Bad Request", "Invalid serverID! This server does not exist yet. " +
                            "If you want to create a new Server please use `api/v1/createServer`"));
        }

        serverService.updateServer(server);


        return DefaultResponse.createResponse(server, "Updated Server");
    }

    /**
     * used to get all ServerGroups of a user
     * @param token users' AccessToken as String
     * @return {@code List<ServerGroup>}
     */
    @GetMapping(path = "/serverGroups")
    public Object getServerGroups(@RequestHeader("Access-Token") String token){
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        List<ServerGroup> serverGroups = serverService.getAllServerGroupsForUser(user.getUserID());
        return DefaultResponse.createResponse(serverGroups, "Server-Groups");
    }


    /**
     * handler for saving Connections aka HistoryItems
     * @param token The AccessToken of the User
     * @param historyItem The HistoryItem: NOT NULL fields: device, ip
     *
     * @return the updated HistoryItem
     */
    @PostMapping(path = "/connection")
    public Object saveConnection(@RequestHeader("Access-Token") String token,
                                 @RequestBody HistoryItem historyItem){

        LocalDateTime timestamp = LocalDateTime.now();
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        // check for NOT NULL fields
        if(historyItem.getDevice() == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(400, "Bad Request", "HistoryItem.device must not be null!"));
        }
        if(historyItem.getIp() == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(400, "Bad Request", "HistoryItem.ip must not be null!"));
        }

        // set ID and save
        historyItem.setID(user.getUserID(), timestamp);
        historyItem = historyItemService.saveHistoryItem(historyItem);

        return DefaultResponse.createResponse(historyItem, "Saved HistoryItem (=Connection)");
    }


    /**
     * used to get a history of all connections (of a user)
     * @return List of HistoryItems
     */
    @GetMapping(path = "/connections")
    public Object getConnections(@RequestHeader("Access-Token") String token){
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        List<HistoryItem> historyItems = historyItemService.getHistoryItemsByUser(user);

        return DefaultResponse.createResponse(historyItems, "List of Connections (HistoryItems) aka 'vErLaUf'");
    }





    @GetMapping(path = "/servers")
    public Object getServerDetails(@RequestHeader("Access-Token") String token){
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }



        return ResponseEntity.status(501).body(new DefaultResponse(501, "Not implemented", "TODO"));
    }


}
