package at.termftp.backend.model;


import javax.persistence.*;
import java.time.LocalDate;
import at.termftp.backend.dao.TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "access_tokens")
public class AccessToken {


    @EmbeddedId
    AccessTokenID accessTokenID;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @Column(name = "pc_name")
    private String pcName;




    public AccessToken(User user, LocalDate validUntil, String pcName) {
        String token = TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs.generate();
        this.accessTokenID = new AccessTokenID(token, user.getUserID());
        this.user = user;

        this.validUntil = validUntil;
        this.pcName = pcName;
    }
    public AccessToken() {
    }


    @Override
    public String toString() {
        return "AccessToken{" +
                "accessTokenID=" + accessTokenID +
                ", user=" + user +
                ", validUntil=" + validUntil +
                ", pcName='" + pcName + '\'' +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessToken that = (AccessToken) o;

        if (accessTokenID != null ? !accessTokenID.equals(that.accessTokenID) : that.accessTokenID != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (validUntil != null ? !validUntil.equals(that.validUntil) : that.validUntil != null) return false;
        return pcName != null ? pcName.equals(that.pcName) : that.pcName == null;
    }

    @Override
    public int hashCode() {
        int result = accessTokenID != null ? accessTokenID.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (validUntil != null ? validUntil.hashCode() : 0);
        result = 31 * result + (pcName != null ? pcName.hashCode() : 0);
        return result;
    }




    public AccessTokenID getAccessTokenID() {
        return accessTokenID;
    }

    public void setAccessTokenID(AccessTokenID accessTokenID) {
        this.accessTokenID = accessTokenID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.accessTokenID.setUserID(user.getUserID());
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate gueltigBis) {
        this.validUntil = gueltigBis;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }
}
