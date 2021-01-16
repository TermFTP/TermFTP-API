package at.termftp.backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ServerGroupID implements Serializable {
    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "group_id")
    private UUID groupID;



    public ServerGroupID(UUID userID, UUID groupID) {
        this.userID = userID;
        this.groupID = groupID;
    }

    public ServerGroupID() {
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGroupID that = (ServerGroupID) o;

        if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
        return groupID != null ? groupID.equals(that.groupID) : that.groupID == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (groupID != null ? groupID.hashCode() : 0);
        return result;
    }





    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }
}
