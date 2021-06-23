package at.termftp.backend.service;

import at.termftp.backend.dao.*;
import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.User;
import at.termftp.backend.utils.CustomLogger;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    // region <head>

    // The number of times a string should be hashed / should go through the process of hashing
    private static final int HASHING_DEPTH = 1000;

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

    // endregion

    // region <util>

    /**
     * used to Hash a password using the SHA-256 algorithm (HASHING-DEPTH -times)
     *
     * @param password the password to be hashed
     * @return the hash
     */
    public static String hashPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < HASHING_DEPTH; i++) {
            bytes = digest.digest(bytes); // hashing
        }

        return new String(bytes, StandardCharsets.UTF_8).replace(new String(new byte[]{0x00}, StandardCharsets.UTF_8), "");
    }

    // endregion

    // region <create User>

    /**
     * used to create a user
     *
     * @return the user
     */
    public User createUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        user = userRepository.save(user);
        CustomLogger.logDefault("created user: " + user.getUsername());

        serverGroupRepository.save(new ServerGroup("default", user));
        serverGroupRepository.save(new ServerGroup("favourites", user));
        CustomLogger.logDefault("created 2 serverGroups (default, favourites) for user: " + user.getUsername());

        return user;
    }

    // endregion

    // region <delete User>

    /**
     * deletes an user by userID
     * ------------ may throw exception when other tables still refer to this user -> delete from other tables first ------------------s
     *
     * @return the number of deleted users
     */
    public int deleteUser(User user) {
        System.out.println("user_id: " + user.getUserID());
        confirmationTokenRepository.deleteByUser(user.getUserID());
        System.out.println("-- Deleted confirmationToken(s) for user: " + user);
        accessTokenRepository.deleteByUserID(user.getUserID());
        System.out.println("-- Deleted access token(s) for user: " + user);

        return userRepository.deleteByUserID(user.getUserID());
    }

    // endregion

    // region <Get>

    /**
     * used to get a user by username
     *
     * @return the user or null
     */
    public User getUserByName(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }


    /**
     * used to get a single user by and ID
     *
     * @param userID the User's ID
     * @return the user or null
     */
    public User getUserById(UUID userID) {
        return userRepository.findById(userID).orElse(null);
    }

    /**
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // endregion

}
