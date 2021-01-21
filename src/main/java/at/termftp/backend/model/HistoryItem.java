package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "history_items")
public class HistoryItem {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");


    @EmbeddedId
    HistoryItemID historyItemID;

    @Transient
    private String when_transient;

    @Column(name = "device")
    private String device;

    @Column(name = "ip")
    private String ip;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "ssh_port")
    private int sshPort;

    @Column(name = "ftp_port")
    private int ftpPort;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;


    public HistoryItem(LocalDateTime when,
                       String device,
                       String ip,
                       boolean deleted,
                       int sshPort,
                       int ftpPort,
                       String username, User user) {
        this.historyItemID = new HistoryItemID(user.getUserID(), when);
        this.device = device;
        this.ip = ip;
        this.deleted = deleted;
        this.sshPort = sshPort;
        this.ftpPort = ftpPort;
        this.username = username;
        this.user = user;
    }

    public HistoryItem() {
    }

    @Override
    public String toString() {
        return "HistoryItem{" +
                "historyItemID=" + historyItemID +
                ", when='" + when_transient + '\'' +
                ", device='" + device + '\'' +
                ", ip='" + ip + '\'' +
                ", deleted=" + deleted +
                ", sshPort=" + sshPort +
                ", ftpPort=" + ftpPort +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryItem that = (HistoryItem) o;

        if (deleted != that.deleted) return false;
        if (sshPort != that.sshPort) return false;
        if (ftpPort != that.ftpPort) return false;
        if (historyItemID != null ? !historyItemID.equals(that.historyItemID) : that.historyItemID != null)
            return false;
        if (device != null ? !device.equals(that.device) : that.device != null) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return user != null ? user.equals(that.user) : that.user == null;
    }

    @Override
    public int hashCode() {
        int result = historyItemID != null ? historyItemID.hashCode() : 0;
        result = 31 * result + (device != null ? device.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + sshPort;
        result = 31 * result + ftpPort;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    public String getWhen_transient() {
        return when_transient;
    }

    public void setWhen_transient(String when_transient) {
        this.when_transient = when_transient;
    }

    public void setID(UUID userID, LocalDateTime when) {
        this.historyItemID = new HistoryItemID(userID, when);
    }

    public HistoryItemID getHistoryItemID() {
        return historyItemID;
    }

    public void setHistoryItemID(HistoryItemID historyItemID) {
        this.historyItemID = historyItemID;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
