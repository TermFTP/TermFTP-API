package at.termftp.backend.api;

import at.termftp.backend.dao.ErrorMessages;
import at.termftp.backend.model.*;
import at.termftp.backend.model.Error;
import at.termftp.backend.service.AccessTokenService;
import at.termftp.backend.service.ConfirmationTokenService;
import at.termftp.backend.service.EmailSenderService;
import at.termftp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
@CrossOrigin(origins= "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class UserController {

    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserController(UserService userService, AccessTokenService accessTokenService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
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
            return ResponseEntity.status(400).body(new DefaultResponse(400, "Bad Request", ErrorMessages.getInvalidUserID()));
        }

        User user = userService.getUserById(id);
        return user != null ? user : ResponseEntity.status(400).body(new DefaultResponse(400, "Bad Request", ErrorMessages.getInvalidUserID()));
    }


    /**
     * This method returns a list of all users
     * @return List<User>
     */
    @GetMapping(path = "/getUsers")
    public Object getAllUsers(){
        List<User> users = userService.getAllUsers();
        return DefaultResponse.createResponse(users, "List of all Users");
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
            ConfirmationToken confirmationToken = confirmationTokenService.createAndGetConfirmationToken(user.getUserID());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration");
            mailMessage.setFrom("termftp.gmail.com");
            String text = "To confirm your account, please click the following link: "
                    + "http://localhost:8080/api/v1/confirm-account?token="
                    + confirmationToken.getToken();
            mailMessage.setText(text);

            emailSenderService.sendEmail(mailMessage);


        }catch(DataIntegrityViolationException ex){
            String message =  ex.getMostSpecificCause().getMessage();
            return ResponseEntity.status(409).body(new DefaultResponse(409, "Conflict",
                    message.contains("u_username")
                            ? ErrorMessages.getDuplicateUsername()
                            : ErrorMessages.getDuplicateEmail()));
        }catch(MailSendException ex){
            String message =  ex.getMostSpecificCause().getMessage();
            return ResponseEntity.status(409)
                    .body(new DefaultResponse(409, "Conflict", "Email could not be sent: " + message));
        }
        return DefaultResponse.createResponse(createdUser, "Created User");
    }

    /**
     * Tis method verifies a user when he click the link in the email
     * @param confirmationToken
     * @return true if the user was verified successfully
     */
    @GetMapping(path = "/confirm-account")
    public Object verify(@RequestParam("token") String confirmationToken){
        if(confirmationToken == null || confirmationToken.length() <= 0){
            return false;
        }
        boolean result = confirmationTokenService.validate(confirmationToken);
        return DefaultResponse.createResponse(result, "Verification");
    }


    /**
     * used to delete a single user by an ID
     * TODO: check accesstoken
     * @param userID
     * @return number of deleted users (int) or an error (Error)
     */
    @DeleteMapping(path = "/deleteUser/{id}")
    public Object deleteUser(@PathVariable("id") String userID){
        UUID id;
        try{
            id = UUID.fromString(userID);
            int deletedUsers = userService.deleteUser(id);
            return DefaultResponse.createResponse(deletedUsers, "Deleted Users");
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(400).body(new DefaultResponse(400, "Bad Request", ErrorMessages.getInvalidUserID()));
        }
    }
}
