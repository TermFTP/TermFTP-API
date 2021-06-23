package at.termftp.backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class SettingID implements Serializable {

    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "setting_id")
    private String settingID;

    public SettingID() {
    }

    public SettingID(UUID userID, String settingID) {
        this.userID = userID;
        this.settingID = settingID;
    }

    // region <any>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingID settingID1 = (SettingID) o;

        if (userID != null ? !userID.equals(settingID1.userID) : settingID1.userID != null) return false;
        return settingID != null ? settingID.equals(settingID1.settingID) : settingID1.settingID == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (settingID != null ? settingID.hashCode() : 0);
        return result;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getSettingID() {
        return settingID;
    }

    public void setSettingID(String settingID) {
        this.settingID = settingID;
    }

    // endregion
}
