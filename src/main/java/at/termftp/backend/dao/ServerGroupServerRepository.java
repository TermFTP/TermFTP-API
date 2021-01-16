package at.termftp.backend.dao;

import at.termftp.backend.model.ServerGroupServer;
import at.termftp.backend.model.ServerGroupServerID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServerGroupServerRepository extends JpaRepository<ServerGroupServer, ServerGroupServerID> {
}
