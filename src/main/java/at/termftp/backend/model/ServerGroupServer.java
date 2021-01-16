package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "server_group_servers")
public class ServerGroupServer implements Serializable {

    @EmbeddedId
    private ServerGroupServerID serverGroupServerID;

    @ManyToOne
    @MapsId("serverID")
    @JoinColumn(name = "server_id", insertable = false, updatable = false)
    private Server server;

    @ManyToOne
    @MapsId("serverGroupID")
    @JoinColumns({
            @JoinColumn(name = "group_id"),
            @JoinColumn(name = "user_id")
    })
    private ServerGroup serverGroup;




    public ServerGroupServer(ServerGroupServerID serverGroupServerID, Server server, ServerGroup serverGroup) {
        this.serverGroupServerID = serverGroupServerID;
        this.server = server;
        this.serverGroup = serverGroup;
    }

    public ServerGroupServer(Server server, ServerGroup serverGroup) {
        this.server = server;
        this.serverGroup = serverGroup;
        this.serverGroupServerID = new ServerGroupServerID(server.getServerID(), serverGroup.getServerGroupID());
    }

    public ServerGroupServer() {
    }



    @Override
    public String toString() {
        return "ServerGroupServer{" +
                "serverGroupServerID=" + serverGroupServerID +
                ", server=" + server +
                ", serverGroup=" + serverGroup +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGroupServer that = (ServerGroupServer) o;

        if (serverGroupServerID != null ? !serverGroupServerID.equals(that.serverGroupServerID) : that.serverGroupServerID != null)
            return false;
        if (server != null ? !server.equals(that.server) : that.server != null) return false;
        return serverGroup != null ? serverGroup.equals(that.serverGroup) : that.serverGroup == null;
    }

    @Override
    public int hashCode() {
        int result = serverGroupServerID != null ? serverGroupServerID.hashCode() : 0;
        result = 31 * result + (server != null ? server.hashCode() : 0);
        result = 31 * result + (serverGroup != null ? serverGroup.hashCode() : 0);
        return result;
    }




    @JsonIgnore
    public ServerGroupServerID getServerGroupServerID() {
        return serverGroupServerID;
    }

    public void setServerGroupServerID(ServerGroupServerID serverGroupServerID) {
        this.serverGroupServerID = serverGroupServerID;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @JsonIgnore
    public ServerGroup getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(ServerGroup serverGroup) {
        this.serverGroup = serverGroup;
    }
}
