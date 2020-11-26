package at.termftp.backend.api;

import at.termftp.backend.model.AccessToken;
import at.termftp.backend.model.Login;
import at.termftp.backend.model.User;
import at.termftp.backend.service.AccessTokenService;
import at.termftp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class UserController {

    private final UserService userService;
    private final AccessTokenService accessTokenService;

    @Autowired
    public UserController(UserService userService, AccessTokenService accessTokenService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
    }

    @GetMapping(path = "/getUser/{id}")
    public User getUserByID(@PathVariable("id") UUID userID){
        return userService.getUserById(userID);
    }


    @GetMapping(path = "/getUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }


    @PostMapping(path = "/register")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }
}
