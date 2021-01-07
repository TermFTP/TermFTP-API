package at.termftp.backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@IdClass(ServerGroupServer.class)
@Entity
@Table(name = "server_group_servers")
public class ServerGroupServer implements Serializable {
    @Id
    @Column(name = "server_id")
    private UUID serverID;

    @Column(name = "group_id")
    private UUID groupID;

    @Column(name = "user_id")
    private UUID userID;

    public ServerGroupServer(UUID serverID, UUID groupID, UUID userID) {
        this.serverID = serverID;
        this.groupID = groupID;
        this.userID = userID;
    }

    public ServerGroupServer() {
    }

    @Override
    public String toString() {
        return "ServerGroupServer{" +
                "serverID=" + serverID +
                ", groupID=" + groupID +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGroupServer that = (ServerGroupServer) o;

        if (serverID != null ? !serverID.equals(that.serverID) : that.serverID != null) return false;
        if (groupID != null ? !groupID.equals(that.groupID) : that.groupID != null) return false;
        return userID != null ? userID.equals(that.userID) : that.userID == null;
    }

    @Override
    public int hashCode() {
        int result = serverID != null ? serverID.hashCode() : 0;
        result = 31 * result + (groupID != null ? groupID.hashCode() : 0);
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
    }

    public UUID getServerID() {
        return serverID;
    }

    public void setServerID(UUID serverID) {
        this.serverID = serverID;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
