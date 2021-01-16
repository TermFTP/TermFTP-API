package at.termftp.backend.service;

import at.termftp.backend.dao.ConfirmationTokenRepository;
import at.termftp.backend.dao.UserRepository;
import at.termftp.backend.model.ConfirmationToken;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        return confirmationTokenRepository.save(new ConfirmationToken(user));
    }

    /**
     * this method validates the confirmation token of a user (after registration)
     * @param token AT as String
     * @return true if the token is valid
     */
    public boolean validate(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token).orElse(null);
        if(confirmationToken == null){
            return false;
        }
        if(!confirmationToken.getConfirmationTokenID().getToken().equals(token)){
            return false;
        }
        if(!confirmationToken.getValidUntil().isAfter(LocalDate.now())){
            return false;
        }

        User user = userRepository.findUserByUserID(confirmationToken.getUser().getUserID()).orElse(null);
        if(user != null){
            user.setVerified(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }
}
