package at.termftp.backend.service;

import at.termftp.backend.dao.AccessTokenRepository;
import at.termftp.backend.dao.ConfirmationTokenRepository;
import at.termftp.backend.dao.UserRepository;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AccessTokenRepository accessTokenRepository;

    public UserService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository, AccessTokenRepository accessTokenRepository) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * used to get a single user by and ID
     * @param userID
     * @return the user or null
     */
    public User getUserById(UUID userID){
        return userRepository.findById(userID).orElse(null);
    }

    /**
     * @return a list of all users
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * used to create a user
     * @param user
     * @return the user
     */
    public User createUser(User user){
        return userRepository.save(user);
    }

    /**
     * used to get a user by username
     * @param username
     * @return the user or null
     */
    public User getUserByName(String username){
        return userRepository.findUserByUsername(username).orElse(null);
    }

    /**
     * deletes an user by userID
     * ------------ may throw exception when other tables still refer to this user -> delete from other tables first ------------------s
     * @param userID
     * @return the number of deleted users
     */
    public int deleteUser(UUID userID){
        int cToken = confirmationTokenRepository.deleteByUserID(userID);
        System.out.println("-- Deleted " + cToken + " confirmationToken for user: " + userID);
        int aToken = accessTokenRepository.deleteByUserID(userID);
        System.out.println("-- Deleted " + aToken + " access token(s) for user: " + userID);

        // delete from other tables TODO

        return userRepository.deleteByUserID(userID);
    }

}
