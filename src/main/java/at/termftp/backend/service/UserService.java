package at.termftp.backend.service;

import at.termftp.backend.dao.ErrorMessages;
import at.termftp.backend.dao.UserRepository;
import at.termftp.backend.model.Error;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Object getUserById(UUID userID){
        User user = userRepository.findById(userID).orElse(null);
        return user != null ? user : new Error(400, "Bad Request", ErrorMessages.getInvalidUserID());
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public Object getUserByName(String username){
        User user = userRepository.findUserByUsername(username).orElse(null);
        return user != null ? user : new Error(400, "Bad Request", ErrorMessages.getInvalidUsername());
    }

}
