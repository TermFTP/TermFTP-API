package at.termftp.backend.service;

import at.termftp.backend.dao.AccessTokenRepository;
import at.termftp.backend.dao.ErrorMessages;
import at.termftp.backend.model.AccessToken;
import at.termftp.backend.model.Error;
import at.termftp.backend.model.Login;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final UserService userService;



    public AccessTokenService(AccessTokenRepository accessTokenRepository, UserService userService) {
        this.accessTokenRepository = accessTokenRepository;
        this.userService = userService;
    }

    /**
     * This methods checks whether an access token has expired or not
     * @param accessToken AccessToken
     * @return true if the access token is still valid
     */
    private boolean isValid(AccessToken accessToken){
        return accessToken.getValidUntil().isAfter(LocalDate.now());
    }


    /**
     * This method validates the user (login) and creates/updates the access token
     * @param login Login
     * @return AccessToken or Error
     */
    public Object createAndOrGetAccessToken(Login login){

        // get the user by username
        User user = userService.getUserByName(login.getUsername());
        if(user == null){
            return new Error(400, "Bad Request", ErrorMessages.getInvalidUsername());
        }

        // check password
        if(!user.getPassword().equals(login.getPassword())){
            return new Error(401, "Unauthorized", ErrorMessages.getInvalidPassword());
        }



        // check if user has already an access token
        AccessToken accessToken = accessTokenRepository.findAccessTokenByUserID(user.getUserID()).orElse(null);

        if(accessToken != null && isValid(accessToken)){
            System.out.println("AccessToken is still valid");
            return accessToken;
        }
        if(accessToken != null) {
            System.out.println("AccessToken has expired");
        }

        // create a new access token
        System.out.println("Creating AccessToken");
        LocalDate expirationDate = LocalDate.now().plusDays(1);
        accessToken = new AccessToken(user, expirationDate, login.getPcName());


        return accessTokenRepository.save(accessToken);
    }

    /**
     * This method returns a list of all access tokens
     * @return List of access tokens
     */
    public List<AccessToken> getAllAccessTokens(){
        return accessTokenRepository.findAll();
    }


    /**
     * used to find a user that is mapped by an AccessToken
     * @param token AccessToken as String
     * @return User
     */
    public User getUserByAccessToken(String token){
        AccessToken accessToken = accessTokenRepository.findAccessTokenByToken(token).orElse(null);
        if(accessToken != null && isValid(accessToken)){
            return accessToken.getUser();
        }
        return null;
    }

}
