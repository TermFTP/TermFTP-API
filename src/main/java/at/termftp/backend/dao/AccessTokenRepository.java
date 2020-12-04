package at.termftp.backend.dao;

import at.termftp.backend.model.AccessToken;
import at.termftp.backend.model.AccessTokenID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, AccessTokenID> {
    Optional<AccessToken> findAccessTokenByUserID(UUID userID);
    int deleteByUserID(UUID userID);
}
