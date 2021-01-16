package at.termftp.backend.model;

import java.util.List;

public class Group {
    private String groupID;
    private String name;
    private List<String> servers;


    public Group(String groupID, String name, List<String> servers) {
        this.groupID = groupID;
        this.name = name;
        this.servers = servers;
    }

    public Group() {
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupID='" + groupID + '\'' +
                ", name='" + name + '\'' +
                ", servers=" + servers +
                '}';
    }


    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (groupID != null ? !groupID.equals(group.groupID) : group.groupID != null) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        return servers != null ? servers.equals(group.servers) : group.servers == null;
    }

    @Override
    public int hashCode() {
        int result = groupID != null ? groupID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (servers != null ? servers.hashCode() : 0);
        return result;
    }
}
