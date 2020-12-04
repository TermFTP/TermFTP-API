package at.termftp.backend.dao;

import at.termftp.backend.model.ConfirmationToken;
import at.termftp.backend.model.ConfirmationTokenID;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, ConfirmationTokenID> {
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
    int deleteByUserID(UUID userID);
}
