package at.termftp.backend.dao;

import at.termftp.backend.model.ServerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerGroupRepository extends JpaRepository<ServerGroup, UUID> {
    Optional<ServerGroup> findServerGroupByGroupID(UUID groupID);
    Optional<List<ServerGroup>> findServerGroupsByUserID(UUID userID);
    Optional<ServerGroup> findServerGroupByUserIDAndName(UUID userID, String name);
}
