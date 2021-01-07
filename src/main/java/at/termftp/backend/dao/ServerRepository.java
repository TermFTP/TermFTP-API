package at.termftp.backend.dao;

import at.termftp.backend.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServerRepository extends JpaRepository<Server, UUID> {
    Optional<Server> findServerByServerID(UUID serverID);
}
