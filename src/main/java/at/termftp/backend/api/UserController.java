package at.termftp.backend.api;

import at.termftp.backend.dao.ErrorMessages;
import at.termftp.backend.model.AccessToken;
import at.termftp.backend.model.Login;
import at.termftp.backend.model.Error;
import at.termftp.backend.model.User;
import at.termftp.backend.service.AccessTokenService;
import at.termftp.backend.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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


    /**
     * This method returns a user if the ID is correct, otherwise an error
     * @param userID
     * @return User or Error
     */
    @GetMapping(path = "/getUser/{id}")
    public Object getUserByID(@PathVariable("id") String userID){
        UUID id;
        try{
            id = UUID.fromString(userID);
        }catch (IllegalArgumentException ex){
            return new Error(400, "Bad Request", ErrorMessages.getInvalidUserID());
        }

        return userService.getUserById(id);
    }


    /**
     * This method returns a list of all users
     * @return List<User>
     */
    @GetMapping(path = "/getUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * This method creates and returns a new user, if email|username is duplicate -> error
     * @param user
     * @return User or Error
     */
    @PostMapping(path = "/register")
    public Object createUser(@RequestBody User user){
        User createdUser = null;
        try{
            createdUser = userService.createUser(user);

        }catch(DataIntegrityViolationException ex){
            String message =  ex.getMostSpecificCause().getMessage();
            return new Error(409, "Conflict",
                    message.contains("u_username")
                            ? ErrorMessages.getDuplicateUsername()
                            : ErrorMessages.getDuplicateEmail());
        }
        return createdUser;
    }
}
