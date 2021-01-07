package at.termftp.backend.dao;

import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.ServerGroupServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerGroupServerRepository extends JpaRepository<ServerGroupServer, ServerGroupServer> {
    Optional<List<ServerGroupServer>> findServerGroupServersByUserID(UUID userID);
}
