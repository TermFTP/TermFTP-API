package at.termftp.backend.dao;

import at.termftp.backend.model.ConfirmationToken;
import at.termftp.backend.model.ConfirmationTokenID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, ConfirmationTokenID> {
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
