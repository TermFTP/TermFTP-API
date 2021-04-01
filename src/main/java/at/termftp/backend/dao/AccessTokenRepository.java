package at.termftp.backend.dao;

import at.termftp.backend.model.AccessToken;
import at.termftp.backend.model.AccessTokenID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, AccessTokenID> {

    /**
     * used to query an AccessToken by UserID
     * @param userID the ID of the User
     * @return AccessToken
     */
    @Query(value = "SELECT token, user_id, valid_until, pc_name " +
            "FROM access_tokens " +
            "WHERE user_id = ?1 " +
            "ORDER BY valid_until DESC " +
            "LIMIT 1;",
            nativeQuery = true)
    Optional<AccessToken> findAccessTokenByUserID(UUID userID);


    /**
     * deletes all AccessTokens that are mapped to a user
     * @param userID the ID of the User
     */
    @Modifying
    @Query(value = "DELETE FROM access_tokens WHERE user_id = ?1",
            nativeQuery = true)
    void deleteByUserID(UUID userID);


    /**
     * used to query an AccessToken by the Token-String
     * @param token String
     * @return AccessToken
     */
    @Query(value = "SELECT * FROM access_tokens ac WHERE ac.token = ?1",
            nativeQuery = true)
    Optional<AccessToken> findAccessTokenByToken(String token);
}
