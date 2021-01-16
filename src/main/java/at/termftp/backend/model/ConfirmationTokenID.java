package at.termftp.backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ConfirmationTokenID implements Serializable {
    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private UUID userID;



    public ConfirmationTokenID(String token, UUID user_id) {
        this.token = token;
        this.userID = user_id;
    }

    public ConfirmationTokenID() {
    }



    @Override
    public String toString() {
        return "ConfirmationTokenID{" +
                "token='" + token + '\'' +
                ", user_id=" + userID +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfirmationTokenID that = (ConfirmationTokenID) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return userID != null ? userID.equals(that.userID) : that.userID == null;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
    }




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
