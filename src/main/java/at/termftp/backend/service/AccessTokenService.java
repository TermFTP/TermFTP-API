package at.termftp.backend.service;

import at.termftp.backend.dao.AccessTokenRepository;
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

    private boolean isValid(AccessToken accessToken){
        return accessToken.getGueltigBis().isAfter(LocalDate.now());
    }


    public Object createAndOrGetAccessToken(Login login){

        User user = userService.getUserByName(login.getUsername());


        if(user == null || !user.getPassword().equals(login.getPassword())){
            return new Error(401, "Unauthorized", "Invalid Username or Password");
        }

        AccessToken accessToken = accessTokenRepository.findAccessTokenByUserID(user.getUserID()).orElse(null);

        if(accessToken != null && isValid(accessToken)){
            System.out.println("AccessToken is still valid");
            return accessToken;
        }
        if(accessToken != null){
            System.out.println("AccessToken has expired");
            accessToken.setGueltigBis(LocalDate.now().plusDays(1));
            return accessTokenRepository.save(accessToken);
        }

        System.out.println("Creating AccessToken");
        LocalDate expirationDate = LocalDate.now().plusDays(1);
        accessToken = new AccessToken(user.getUserID(), expirationDate, login.getPcName());


        return accessTokenRepository.save(accessToken);
    }

    public List<AccessToken> getAllAccessTokens(){
        return accessTokenRepository.findAll();
    }
}
