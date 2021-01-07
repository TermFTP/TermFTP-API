package at.termftp.backend.api;

import at.termftp.backend.model.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class ServerController {

    @Autowired
    public ServerController() {
    }

    // TODO
    /**
     * used to set some servers as favourite ones
     * @param server list of server id's
     * @return true or false
     */
    @PostMapping(path = "/favourite")
    public Object setFavouriteServers(@RequestBody List<String> server){
        return ResponseEntity.status(501).body(new DefaultResponse(501, "Not implemented", false));
    }
}
