package at.termftp.backend.api;

import at.termftp.backend.model.DefaultResponse;
import at.termftp.backend.model.Login;
import at.termftp.backend.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.PUT})
public class AccessController {
    private final AccessTokenService accessTokenService;

    @Autowired
    public AccessController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }


    /**
     * This method returns an AccessToken or an Error if the login failed
     * @return AccessToken or Error
     */
    @PostMapping(path = "/login")
    public Object login(@RequestBody Login login,
                        @RequestHeader("PC-Name") String pcName){
        if(pcName == null || pcName.length() == 0){
            return ResponseEntity.status(400).body(new DefaultResponse(400, "Bad Request", "PC-Name must be set (header)!"));
        }
        login.setPcName(pcName);
        Object atOrError = accessTokenService.createAndOrGetAccessToken(login);
        return DefaultResponse.createResponse(atOrError, "AccessToken");
    }
}
