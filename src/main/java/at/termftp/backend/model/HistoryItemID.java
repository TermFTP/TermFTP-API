package at.termftp.backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
public class HistoryItemID implements Serializable {

    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "`when`") // when is a key word
    private LocalDateTime when;

    public HistoryItemID(UUID userID, LocalDateTime when) {
        this.userID = userID;
        this.when = when;
    }

    public HistoryItemID() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryItemID that = (HistoryItemID) o;

        if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
        return when != null ? when.equals(that.when) : that.when == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (when != null ? when.hashCode() : 0);
        return result;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }
}
