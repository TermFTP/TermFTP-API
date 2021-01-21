package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
    private String username;
    private String password;
    private String pcName;

    public Login() {
    }

    public Login(@JsonProperty("username") String username,
                 @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", pcName='" + pcName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login = (Login) o;

        if (username != null ? !username.equals(login.username) : login.username != null) return false;
        if (password != null ? !password.equals(login.password) : login.password != null) return false;
        return pcName != null ? pcName.equals(login.pcName) : login.pcName == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (pcName != null ? pcName.hashCode() : 0);
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }
}
