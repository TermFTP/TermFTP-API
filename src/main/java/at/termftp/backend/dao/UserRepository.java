package at.termftp.backend.dao;

import at.termftp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * used to get a User by its name
     * @param username :String
     * @return User
     */
    Optional<User> findUserByUsername(String username);

    /**
     * used to get a User by its ID
     * @param userID :UUID
     * @return User
     */
    Optional<User> findUserByUserID(UUID userID);

    /**
     * used to delete a User by its ID
     * @param userID :UUID
     * @return the number of deleted users (0 or 1)
     */
    int deleteByUserID(UUID userID);
}
