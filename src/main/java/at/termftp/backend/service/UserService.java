package at.termftp.backend.service;

import at.termftp.backend.dao.*;
import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final ServerGroupRepository serverGroupRepository;

    public UserService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository, AccessTokenRepository accessTokenRepository, ServerGroupRepository serverGroupRepository) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.serverGroupRepository = serverGroupRepository;
    }

    /**
     * used to get a single user by and ID
     * @param userID the User's ID
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
     * @return the user
     */
    public User createUser(User user){
        user = userRepository.save(user);
        serverGroupRepository.save(new ServerGroup("default", user));
        serverGroupRepository.save(new ServerGroup("favourites", user));
        return user;
    }

    /**
     * used to get a user by username
     * @return the user or null
     */
    public User getUserByName(String username){
        return userRepository.findUserByUsername(username).orElse(null);
    }

    /**
     * deletes an user by userID
     * ------------ may throw exception when other tables still refer to this user -> delete from other tables first ------------------s
     * @return the number of deleted users
     */
    public int deleteUser(User user){
        int cToken = confirmationTokenRepository.deleteByUser(user.getUserID());
        System.out.println("-- Deleted " + cToken + " confirmationToken for user: " + user);
        int aToken = accessTokenRepository.deleteByUserID(user.getUserID());
        System.out.println("-- Deleted " + aToken + " access token(s) for user: " + user);

        // delete from other tables TODO

        return userRepository.deleteByUserID(user.getUserID());
    }

}
