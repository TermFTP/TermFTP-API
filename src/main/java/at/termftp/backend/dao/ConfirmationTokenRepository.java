package at.termftp.backend.dao;

import at.termftp.backend.model.ConfirmationToken;
import at.termftp.backend.model.ConfirmationTokenID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, ConfirmationTokenID> {

    /**
     * used to query a ConfirmationToken by the token-String
     * @param token confirmationToken as String
     * @return ConfirmationToken
     */
    @Query(value = "SELECT * FROM confirmation_tokens WHERE token = ?1",
            nativeQuery = true)
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);


    /**
     * deletes all ConfirmationTokens that are mapped to a user
     * @param userID :UUID the ID of the User
     * @return the number of deleted ConfirmationTokens
     */
    @Query(value = "DELETE FROM confirmation_tokens WHERE user_id = ?1",
            nativeQuery = true)
    int deleteByUser(UUID userID);
}
