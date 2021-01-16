package at.termftp.backend.dao;

import at.termftp.backend.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServerRepository extends JpaRepository<Server, UUID> {

    /**
     * used to get a Server by its ID
     * @param serverID :UUID the ID of the Server
     * @return Server
     */
    Optional<Server> findServerByServerID(UUID serverID);
}
