package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "server_groups")
public class ServerGroup {
//    group_id uuid NOT NULL,
//    name character varying(256) COLLATE pg_catalog."default",
//    user_id uuid NOT NULL,

    @Id
    @Column(name = "group_id")
    private UUID groupID;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private UUID userID;

    public ServerGroup(@JsonProperty("groupID") UUID groupID,
                       @JsonProperty("name") String name,
                       @JsonProperty("userID") UUID userID) {
        this.groupID = groupID;
        this.name = name;
        this.userID = userID;
    }

    public ServerGroup() {
    }

    public ServerGroup(String name, UUID userID) {
        this.name = name;
        this.userID = userID;
        this.groupID = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "ServerGroup{" +
                "groupID=" + groupID +
                ", name='" + name + '\'' +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGroup that = (ServerGroup) o;

        if (groupID != null ? !groupID.equals(that.groupID) : that.groupID != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return userID != null ? userID.equals(that.userID) : that.userID == null;
    }

    @Override
    public int hashCode() {
        int result = groupID != null ? groupID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
