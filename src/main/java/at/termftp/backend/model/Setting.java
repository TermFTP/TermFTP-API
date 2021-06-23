package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "settings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setting {

    @EmbeddedId
    SettingID settingID;

    @Column(name = "value")
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    // region <constructor>

    public Setting() {
    }

    public Setting(SettingID settingID, String value, User user) {
        this.settingID = settingID;
        this.value = value;
        this.user = user;
    }

    public Setting(String key, String value, User user) {
        this.value = value;
        this.user = user;
        this.settingID = new SettingID(user.getUserID(), key);
    }

    // endregion

    // region <any>

    @Override
    public String toString() {
        return "Setting{" +
                "settingID=" + settingID.getSettingID() +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Setting setting = (Setting) o;

        if (settingID != null ? !settingID.equals(setting.settingID) : setting.settingID != null) return false;
        if (value != null ? !value.equals(setting.value) : setting.value != null) return false;
        return user != null ? user.equals(setting.user) : setting.user == null;
    }

    @Override
    public int hashCode() {
        int result = settingID != null ? settingID.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public SettingID getSettingID() {
        return settingID;
    }

    public void setSettingID(SettingID settingID) {
        this.settingID = settingID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // endregion
}
