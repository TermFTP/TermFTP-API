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
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class SettingsController {
    private static final Path schema = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "settings_schema.json");
    private static final Path jsonFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "settings.json");

    @Autowired
    public SettingsController() {
    }

    @GetMapping(path = "/settings")
    public Object parseSettings(){



        boolean result = false;
        try {
            result = SettingsParser.validate(jsonFile, schema);
        } catch (IOException | ValidationException e) {
            return ResponseEntity.status(409).body(new DefaultResponse(409, "The settings-file is INVALID!!!", e.getMessage()));
        }

        return ResponseEntity.status(200).body(DefaultResponse.createResponse(result, result ? "The settings-file seems to be valid." : "The file does not exist!"));
    }

    @GetMapping(path = "/settings/get")
    public Object getSetting(){
        Object property = null;
        try {
            property = SettingsParser.getProperty(jsonFile, "profiles.0", true, schema);
        } catch (IOException | ValidationException e) {
            return ResponseEntity.status(409).body(new DefaultResponse(409, "The settings-file is INVALID!!!", e.getMessage()));
        }
        return ResponseEntity.status(200).body(DefaultResponse.createResponse(property, "Experimental - TODO - Not completely tested yet!"));
    }


}
