package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "verified")
    private boolean verified;

    @JsonIgnore // preventing infinite recursion
    @OneToMany(mappedBy = "user", targetEntity = ServerGroup.class, cascade = CascadeType.REMOVE)
    private List<ServerGroup> serverGroups;

    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = Setting.class, cascade = CascadeType.REFRESH)
    private List<Setting> settings;


    public User(UUID userID, String username, String email, String password, boolean verified, List<ServerGroup> serverGroups) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.serverGroups = serverGroups;
    }

    public User(@JsonProperty("username") String username,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.userID = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = false;
    }

    public User() {
    }


    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                ", serverGroups=" + serverGroups +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (verified != user.verified) return false;
        if (userID != null ? !userID.equals(user.userID) : user.userID != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return serverGroups != null ? serverGroups.equals(user.serverGroups) : user.serverGroups == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (verified ? 1 : 0);
        result = 31 * result + (serverGroups != null ? serverGroups.hashCode() : 0);
        return result;
    }





    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<ServerGroup> getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(List<ServerGroup> serverGroups) {
        this.serverGroups = serverGroups;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }
}
