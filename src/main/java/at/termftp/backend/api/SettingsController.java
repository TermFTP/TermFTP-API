package at.termftp.backend.api;

import at.termftp.backend.bl.SettingsParser;
import at.termftp.backend.model.DefaultResponse;
import org.everit.json.schema.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.PUT})
public class SettingsController {

    // region <head>

    private static final Path schema = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "settings_schema.json");
    private static final Path jsonFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "settings.json");

    @Autowired
    public SettingsController() {
    }

    // endregion

    // region <Get>

    /**
     * endpoint to retrieve the local settings file
     * @return a defaultResponse with the jsonString
     */
    @GetMapping(path = "/settings")
    public Object getSettings(){
        try {
            String settings = SettingsParser.getCompleteJsonFile(jsonFile, schema);
            return ResponseEntity.status(200).body(DefaultResponse.createResponse(settings, ".. and here are the settings."));

        } catch (IOException | ValidationException e) {
            return ResponseEntity.status(409).body(new DefaultResponse(409, "The local settings-file is INVALID!!!", e.getMessage()));
        }
    }

    // endregion

    // region <save/change settings>

    /**
     * endpoint to save settings into a local settings-file (json)
     * @param jsonString the jsonString of settings
     * @return a defaultResponse with the status
     */
    @PostMapping(path = "/settings")
    public Object saveSettings(@RequestBody String jsonString){

        try {
            boolean success = SettingsParser.saveSettings(jsonFile, schema, jsonString);
            if(success){
                return ResponseEntity.status(200).body(DefaultResponse.createResponse(success, "Successfully saved settings."));
            }else{
                return ResponseEntity.status(400).body(new DefaultResponse(400, "The jsonString is INVALID!!!", false));
            }

        } catch (IOException | ValidationException e) {
            return ResponseEntity.status(400).body(new DefaultResponse(400, "The jsonString is INVALID!!!", false));
        }
    }

    // endregion

}
