package at.termftp.backend.model;

import org.hibernate.annotations.Columns;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ServerGroupServerID implements Serializable {

    @Column(name = "server_id")
    private UUID serverID;

    @Columns(columns = {
            @Column(name = "group_id"),
            @Column(name = "user_id")
    })
    private ServerGroupID serverGroupID;




    public ServerGroupServerID(UUID serverID, ServerGroupID serverGroupID) {
        this.serverID = serverID;
        this.serverGroupID = serverGroupID;
    }

    public ServerGroupServerID() {
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGroupServerID that = (ServerGroupServerID) o;

        if (serverID != null ? !serverID.equals(that.serverID) : that.serverID != null) return false;
        return serverGroupID != null ? serverGroupID.equals(that.serverGroupID) : that.serverGroupID == null;
    }

    @Override
    public int hashCode() {
        int result = serverID != null ? serverID.hashCode() : 0;
        result = 31 * result + (serverGroupID != null ? serverGroupID.hashCode() : 0);
        return result;
    }




    public UUID getServerID() {
        return serverID;
    }

    public void setServerID(UUID serverID) {
        this.serverID = serverID;
    }

    public ServerGroupID getServerGroupID() {
        return serverGroupID;
    }

    public void setServerGroupID(ServerGroupID serverGroupID) {
        this.serverGroupID = serverGroupID;
    }
}
