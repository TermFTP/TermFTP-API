package at.termftp.backend.api;

import at.termftp.backend.model.*;
import at.termftp.backend.service.AccessTokenService;
import at.termftp.backend.service.SettingService;
import at.termftp.backend.utils.CustomLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= {"http://localhost:3000", "http://localhost:14000"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.PUT})
public class SettingsController {

    // region <head>

    private final AccessTokenService accessTokenService;
    private final SettingService settingService;

    @Autowired
    public SettingsController(AccessTokenService accessTokenService, SettingService settingService) {
        this.accessTokenService = accessTokenService;
        this.settingService = settingService;
    }

    // endregion


    /**
     * endpoint used to get ALL settings for a user identified by his access-token
     * @param token the users' Access-Token
     * @return the list of saved Settings for a User
     */
    @GetMapping(path = "/settings")
    public Object getSettings(@RequestHeader("Access-Token") String token){
        // validate AccessToken
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            CustomLogger.logCustom(0, Level.WARNING, "/settings: Invalid Access-Token");
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        // convert to simple setting
        List<SimpleSetting> settings = settingService.getAllSettingsForUser(user.getUserID())
                .stream()
                .map(s -> new SimpleSetting(s.getSettingID().getSettingID(), s.getValue()))
                .collect(Collectors.toList());


        CustomLogger.logDefault("returning all settings for user " + user.getUsername());
        return DefaultResponse.createResponse(settings, "All settings for user " + user.getUsername());
    }


    // region <save/change>

    /**
     * used to parse a setting-string into a list of Settings
     * @param jsonString the json-string containing a list of SimpleSettings
     * @param user the user the settings belong to
     * @return a list of settings
     * @throws JsonProcessingException if there's sth wrong with the json-string
     */
    private List<Setting> parseSettings(String jsonString, User user) throws JsonProcessingException {
        JsonMapper mapper = new JsonMapper();

        List<SimpleSetting> simpleSettings = mapper.readValue(jsonString, new TypeReference<>() {});

        // add user
        return simpleSettings.stream()
                .map(ss -> new Setting(ss.getSettingID(), ss.getValue(), user))
                .collect(Collectors.toList());
    }


    /**
     * endpoint used to save/change settings for a user
     * @param token the users' Access-Token
     * @param jsonString the json-string containing a list of SimpleSettings
     * @return the list of settings
     */
    @PostMapping(path = "/settings")
    public Object saveSettings(@RequestHeader("Access-Token") String token,
                               @RequestBody String jsonString){
        // validate AccessToken
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            CustomLogger.logCustom(0, Level.WARNING, "/settings: Invalid Access-Token");
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        try {
            List<Setting> settings = parseSettings(jsonString, user);
            int savedSettings = settingService.saveManySettings(settings);
            CustomLogger.logDefault("Saved " + savedSettings + " settings.");
            return DefaultResponse.createResponse(settings, "Saved " + savedSettings + " settings.");

        } catch (JsonProcessingException e) {
            CustomLogger.logWarning("/settings: problem with json: " + e.getMessage());
            return ResponseEntity.status(400).
                    body(new DefaultResponse(400, "Bad Request. Problem with json.", e.getMessage()));
        }
    }

    // endregion



    /**
     * endpoint used to delete (many) settings
     * @param token the users' Access-Token
     * @param keys the list of settingIDs (= keys)
     * @return the number of deleted settings
     */
    @DeleteMapping(path = "/deleteSettings")
    public Object deleteSettings(@RequestHeader("Access-Token") String token,
                                 @RequestBody List<String> keys){
        // validate AccessToken
        User user = accessTokenService.getUserByAccessToken(token);
        if(user == null){
            CustomLogger.logCustom(0, Level.WARNING, "/settings: Invalid Access-Token");
            return ResponseEntity.status(401)
                    .body(new DefaultResponse(401, "Unauthorized", "Invalid Access-Token"));
        }

        // map to settingID
        List<SettingID> settingIDs = keys.stream()
                .map(key -> new SettingID(user.getUserID(), key))
                .collect(Collectors.toList());

        // delete
        int deletedSettings = settingService.deleteManySettings(settingIDs);
        CustomLogger.logDefault("Deleted " + deletedSettings + " settings.");

        return DefaultResponse.createResponse(deletedSettings, "Deleted Settings for user " + user.getUsername());
    }



}
