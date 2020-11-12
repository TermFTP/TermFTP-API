package at.termftp.backend.api;

import at.termftp.backend.dao.UserRepository;
import at.termftp.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/getUser/{id}")
    public User getUserByID(@PathVariable("id") UUID userID){
        return userRepository.findById(userID).orElse(null);
    }

    @GetMapping(path = "/getUsers")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping(path = "/register")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }
}
