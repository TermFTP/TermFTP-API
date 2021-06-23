package at.termftp.backend.model;

public class SimpleSetting {
    private String settingID;
    private String value;

    public SimpleSetting(String settingID, String value) {
        this.settingID = settingID;
        this.value = value;
    }

    public SimpleSetting() {
    }

    @Override
    public String toString() {
        return "SimpleSetting{" +
                "settingID='" + settingID + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleSetting that = (SimpleSetting) o;

        if (settingID != null ? !settingID.equals(that.settingID) : that.settingID != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = settingID != null ? settingID.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String getSettingID() {
        return settingID;
    }

    public void setSettingID(String settingID) {
        this.settingID = settingID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
