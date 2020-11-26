package at.termftp.backend.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import at.termftp.backend.dao.TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs;

@IdClass(AccessTokenID.class)
@Entity
@Table(name = "access_tokens")
public class AccessToken {

    @Id
    @Column(name = "token")
    private String token;

    @Id
    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    @Column(name = "pc_name")
    private String pcName;

    public AccessToken(UUID userID, LocalDate gueltigBis, String pcName) {
        this.token = TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs.generate();
        this.userID = userID;
        this.gueltigBis = gueltigBis;
        this.pcName = pcName;
    }



    public AccessToken() {
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", userID=" + userID +
                ", gueltigBis=" + gueltigBis +
                ", pcName='" + pcName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessToken that = (AccessToken) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
        if (gueltigBis != null ? !gueltigBis.equals(that.gueltigBis) : that.gueltigBis != null) return false;
        return pcName != null ? pcName.equals(that.pcName) : that.pcName == null;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        result = 31 * result + (gueltigBis != null ? gueltigBis.hashCode() : 0);
        result = 31 * result + (pcName != null ? pcName.hashCode() : 0);
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

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }
}
