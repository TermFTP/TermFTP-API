package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "server_groups")
public class ServerGroup {

    @EmbeddedId
    private ServerGroupID serverGroupID;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "serverGroup", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ServerGroupServer> serverGroupServers;



    @OneToMany(mappedBy = "parentGroup", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<ServerGroup> serverGroups = new HashSet<>();

    @ManyToOne
//    @JoinColumns({
//            @JoinColumn(name = "group_id", insertable = false, updatable = false),
//            @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    })
    private ServerGroup parentGroup;





    public UUID getUserID(){
        return serverGroupID.getUserID();
    }
    public UUID getGroupID(){
        return serverGroupID.getGroupID();
    }

    public List<Server> getServer(){
        return serverGroupServers.stream().map(ServerGroupServer::getServer).collect(Collectors.toList());
    }




    public ServerGroup(@JsonProperty("groupID") ServerGroupID serverGroupID,
                       @JsonProperty("name") String name,
                       @JsonProperty("userID") User user) {
        this.serverGroupID = serverGroupID;
        this.name = name;
        this.user = user;
    }
    public ServerGroup(String name, User user) {
        this.name = name;
        this.user = user;
        this.serverGroupID = new ServerGroupID(user.getUserID(), UUID.randomUUID());
        this.serverGroupServers = new ArrayList<>();
        this.serverGroups = new HashSet<>();
    }
    public ServerGroup() {
    }


    @Override
    public String toString() {
        return "ServerGroup{" +
                "serverGroupID=" + serverGroupID +
                ", name='" + name + '\'' +
                ", serverGroups=" + serverGroups.stream().map(sg -> sg.getServerGroupID()).collect(Collectors.toList()) +
                '}';
    }

    @JsonIgnore
    public ServerGroupID getServerGroupID() {
        return serverGroupID;
    }

    public void setServerGroupID(ServerGroupID serverGroupID) {
        this.serverGroupID = serverGroupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public List<ServerGroupServer> getServerGroupServers() {
        return serverGroupServers;
    }

    public void setServerGroupServers(List<ServerGroupServer> servers) {
        this.serverGroupServers = servers;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonManagedReference
    public Set<ServerGroup> getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(Set<ServerGroup> serverGroups) {
        this.serverGroups = serverGroups;
    }

    @JsonBackReference
    public ServerGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(ServerGroup parentGroup) {
        this.parentGroup = parentGroup;
    }
}
