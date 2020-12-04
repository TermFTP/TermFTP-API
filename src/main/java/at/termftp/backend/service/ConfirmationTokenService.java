package at.termftp.backend.service;

import at.termftp.backend.dao.ConfirmationTokenRepository;
import at.termftp.backend.dao.UserRepository;
import at.termftp.backend.model.ConfirmationToken;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

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
     * @param userID
     * @return the confirmation token
     */
    public ConfirmationToken createAndGetConfirmationToken(UUID userID){
        return confirmationTokenRepository.save(new ConfirmationToken(userID));
    }

    /**
     * this method validates the confirmation token of a user (after registration)
     * @param token
     * @return true if the token is valid
     */
    public boolean validate(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token).orElse(null);
        if(confirmationToken == null){
            return false;
        }
        if(!confirmationToken.getToken().equals(token)){
            return false;
        }
        if(!confirmationToken.getGueltigBis().isAfter(LocalDate.now())){
            return false;
        }

        User user = userRepository.findUserByUserID(confirmationToken.getUserID()).orElse(null);
        if(user != null){
            user.setVerified(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }
}
