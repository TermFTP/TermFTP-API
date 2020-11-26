package at.termftp.backend.api;

import at.termftp.backend.model.User;
import at.termftp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
