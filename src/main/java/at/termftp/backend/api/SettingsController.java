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

    @Autowired
    public SettingsController() {
    }

    @GetMapping(path = "/settings")
    public Object parseSettings(){

        Path schema = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "settings_schema.json");
        Path jsonFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "settings.json");

        boolean result = false;
        try {
            result = SettingsParser.validate(jsonFile, schema);
        } catch (IOException | ValidationException e) {
            return ResponseEntity.status(200).body(DefaultResponse.createResponse(e.getMessage(), "The settings-file is INVALID!!!"));
        }

        return ResponseEntity.status(200).body(DefaultResponse.createResponse(result, result ? "The settings-file seems to be valid." : "The file does not exist!"));
    }


}
