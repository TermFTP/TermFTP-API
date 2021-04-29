package at.termftp.backend.dao;

import at.termftp.backend.model.Setting;
import at.termftp.backend.model.SettingID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface SettingsRepository extends JpaRepository<Setting, SettingID> {

    /**
     * used to query all saved Settings for an User
     * @param userID the ID of the User
     * @return List of Settings
     */
    @Query(value = "SELECT user_id, setting_id, value " +
            "FROM settings " +
            "WHERE user_id = ?1 ;",
            nativeQuery = true)
    Optional<List<Setting>> findAllByUserID(UUID userID);

    /**
     * used to delete a single Setting entry
     * @param settingID the ID (user_id, setting_id)
     * @return the number of deleted settings (0 or 1)
     */
    int deleteBySettingID(SettingID settingID);
}
