package at.termftp.backend.model;

import at.termftp.backend.dao.TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@IdClass(ConfirmationTokenID.class)
@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @Column(name = "token")
    private String token;

    @Id
    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    public ConfirmationToken(UUID userID) {
        this.userID = userID;
        this.token = TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs.generate();
        this.gueltigBis = LocalDate.now().plusDays(1);
    }

    public ConfirmationToken() {
    }

    @Override
    public String toString() {
        return "ConfirmationToken{" +
                "token='" + token + '\'' +
                ", userID=" + userID +
                ", gueltigBis=" + gueltigBis +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfirmationToken that = (ConfirmationToken) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
        return gueltigBis != null ? gueltigBis.equals(that.gueltigBis) : that.gueltigBis == null;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        result = 31 * result + (gueltigBis != null ? gueltigBis.hashCode() : 0);
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
}
