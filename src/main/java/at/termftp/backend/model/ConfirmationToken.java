package at.termftp.backend.model;

import at.termftp.backend.dao.TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken {

    @EmbeddedId
    ConfirmationTokenID confirmationTokenID;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false) //ch_1
    private User user;

    @Column(name = "valid_until")
    private LocalDate validUntil;




    public ConfirmationToken(User user) {
        this.user = user;
        String token = TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs.generate();
        this.confirmationTokenID = new ConfirmationTokenID(token, user.getUserID());

        this.validUntil = LocalDate.now().plusDays(1);
    }

    public ConfirmationToken() {
    }


    @Override
    public String toString() {
        return "ConfirmationToken{" +
                "confirmationTokenID=" + confirmationTokenID +
                ", validUntil=" + validUntil +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfirmationToken that = (ConfirmationToken) o;

        if (confirmationTokenID != null ? !confirmationTokenID.equals(that.confirmationTokenID) : that.confirmationTokenID != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return validUntil != null ? validUntil.equals(that.validUntil) : that.validUntil == null;
    }

    @Override
    public int hashCode() {
        int result = confirmationTokenID != null ? confirmationTokenID.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (validUntil != null ? validUntil.hashCode() : 0);
        return result;
    }



    public ConfirmationTokenID getConfirmationTokenID() {
        return confirmationTokenID;
    }

    public void setConfirmationTokenID(ConfirmationTokenID confirmationTokenID) {
        this.confirmationTokenID = confirmationTokenID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.confirmationTokenID.setUserID(user.getUserID());
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate gueltigBis) {
        this.validUntil = gueltigBis;
    }
}
