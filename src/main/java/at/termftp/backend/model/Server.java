package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "servers")
public class Server {
    @Id
    @Column(name = "server_id")
    private UUID serverID;

    @Column(name = "ip")
    private String ip;

    @Column(name = "ftp_port")
    private int ftpPort;

    @Column(name = "ssh_port")
    private int sshPort;

    @Column(name = "last_connection")
    private LocalDate lastConnection;

    @JsonIgnore
    @OneToMany(mappedBy = "server")
    private List<ServerGroupServer> serverGroupServers;


    public Server(@JsonProperty("serverID") UUID serverID,
                  @JsonProperty("ip") String ip,
                  @JsonProperty("ftpPort") int ftpPort,
                  @JsonProperty("sshPort") int sshPort,
                  @JsonProperty("lastConnection") LocalDate lastConnection) {
        if(serverID == null){
            serverID = UUID.randomUUID();
        }
        this.serverID = serverID;
        this.ip = ip;
        this.ftpPort = ftpPort;
        this.sshPort = sshPort;
        this.lastConnection = lastConnection;
    }

    public Server(String ip) {
        this.ip = ip;
        this.ftpPort = -1;
        this.sshPort = -1;
        this.lastConnection = null;
        this.serverID = UUID.randomUUID();
    }

    public Server() {
    }



    @Override
    public String toString() {
        return "Server{" +
                "serverID=" + serverID +
                ", ip='" + ip + '\'' +
                ", ftpPort=" + ftpPort +
                ", sshPort=" + sshPort +
                ", lastConnection=" + lastConnection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (ftpPort != server.ftpPort) return false;
        if (sshPort != server.sshPort) return false;
        if (serverID != null ? !serverID.equals(server.serverID) : server.serverID != null) return false;
        if (ip != null ? !ip.equals(server.ip) : server.ip != null) return false;
        return lastConnection != null ? lastConnection.equals(server.lastConnection) : server.lastConnection == null;
    }

    @Override
    public int hashCode() {
        int result = serverID != null ? serverID.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + ftpPort;
        result = 31 * result + sshPort;
        result = 31 * result + (lastConnection != null ? lastConnection.hashCode() : 0);
        return result;
    }




    @JsonIgnore
    public List<ServerGroupServer> getServerGroupServers() {
        return serverGroupServers;
    }

    public void setServerGroupServers(List<ServerGroupServer> serverGroups) {
        this.serverGroupServers = serverGroups;
    }

    public UUID getServerID() {
        return serverID;
    }

    public void setServerID(UUID serverID) {
        this.serverID = serverID;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public LocalDate getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(LocalDate lastConnection) {
        this.lastConnection = lastConnection;
    }
}
