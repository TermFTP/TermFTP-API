package at.termftp.backend.service;

import at.termftp.backend.dao.SettingsRepository;
import at.termftp.backend.model.Setting;
import at.termftp.backend.model.SettingID;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SettingService {
    private final SettingsRepository settingsRepository;

    public SettingService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public List<Setting> getAllSettingsForUser(UUID userID){
        return settingsRepository.findAllByUserID(userID).orElse(null);
    }

    public Setting saveSetting(Setting setting){
        return settingsRepository.save(setting);
    }

    public int saveManySettings(List<Setting> settings){
        return settings.stream().mapToInt(s -> {
            Setting setting = this.saveSetting(s);
            return setting == null ? 0 : 1;
        }).sum();
    }

    public int deleteASingleSetting(SettingID settingID){
        return settingsRepository.deleteBySettingID(settingID);
    }

    public int deleteManySettings(List<SettingID> settingIDs){
        return settingIDs.stream().mapToInt(this::deleteASingleSetting).sum();
    }

}
