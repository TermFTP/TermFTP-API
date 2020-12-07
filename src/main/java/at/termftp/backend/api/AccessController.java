package at.termftp.backend.api;

import at.termftp.backend.model.Error;
import at.termftp.backend.model.Login;
import at.termftp.backend.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class AccessController {
    private final AccessTokenService accessTokenService;

    @Autowired
    public AccessController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }


    /**
     * This method returns an AccessToken or an Error if the login failed
     * @param login
     * @return AccessToken or Error
     */
    @PostMapping(path = "/login")
    public Object login(@RequestBody Login login){
        Object atOrError = accessTokenService.createAndOrGetAccessToken(login);
        return ResponseEntity.status(atOrError instanceof Error ? ((Error)atOrError).getStatus() : HttpStatus.OK.value())
                .body(atOrError);
    }
}
