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
public class ResourceController {

    @Autowired
    public ResourceController() {
    }



}
