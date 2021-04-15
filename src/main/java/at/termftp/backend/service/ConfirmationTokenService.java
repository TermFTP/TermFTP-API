package at.termftp.backend.service;

import at.termftp.backend.dao.ConfirmationTokenRepository;
import at.termftp.backend.dao.UserRepository;
import at.termftp.backend.model.ConfirmationToken;
import at.termftp.backend.model.User;
import at.termftp.backend.utils.CustomLogger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Level;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * used to create a confirmation token
     * @param user User
     * @return the confirmation token
     */
    public ConfirmationToken createAndGetConfirmationToken(User user){
        ConfirmationToken confirmationToken = confirmationTokenRepository.save(new ConfirmationToken(user));
        CustomLogger.logDefault("created confirmation token for user: " + user.getUsername());
        return confirmationToken;
    }

    /**
     * this method validates the confirmation token of a user (after registration)
     * @param token AT as String
     * @return true if the token is valid
     */
    public boolean validate(String token){
        CustomLogger.logDefault("validating confirmation token...");
        ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token).orElse(null);
        if(confirmationToken == null){
            CustomLogger.logCustom(1, Level.WARNING, "confirmation token does not match");
            return false;
        }

        if(!confirmationToken.getValidUntil().isAfter(LocalDate.now())){
            CustomLogger.logCustom(1, Level.WARNING, "confirmation token is no longer valid");
            return false;
        }

        User user = userRepository.findUserByUserID(confirmationToken.getUser().getUserID()).orElse(null);
        if(user != null){
            user.setVerified(true);
            userRepository.save(user);
            CustomLogger.logCustom(1, Level.INFO, "successfully verified user");
            return true;
        }

        return false;
    }
}
