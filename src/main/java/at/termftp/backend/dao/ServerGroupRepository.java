package at.termftp.backend.dao;

import at.termftp.backend.model.ServerGroup;
import at.termftp.backend.model.ServerGroupID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface ServerGroupRepository extends JpaRepository<ServerGroup, ServerGroupID> {

    /**
     * used to query a ServerGroup by its GroupID
     * @param groupID :UUID generated GroupID
     * @return ServerGroup
     */
    @Query(value = "SELECT * FROM server_groups WHERE group_id = ?1",
            nativeQuery = true)
    Optional<ServerGroup> findServerGroupByGroupID(UUID groupID);


    /**
     * used to get a list of all ServerGroups that belong to a user
     * @param userID :UUID the user's ID
     * @return a List of ServerGroups
     */
    @Query(value = "SELECT * FROM server_groups WHERE user_id = ?1 AND parent_group_group_id IS NULL",
            nativeQuery = true)
    Optional<List<ServerGroup>> findServerGroupsByUserID(UUID userID);


    /**
     * used to query a single ServerGroup by name
     * @param userID :UUID the user's ID
     * @param name the name of the server group
     * @return ServerGroup
     */
    @Query(value = "SELECT * FROM server_groups WHERE user_id = ?1 AND name = ?2",
            nativeQuery = true)
    Optional<ServerGroup> findServerGroupByUserIDAndName(UUID userID, String name);



    @Query(value = "SELECT * FROM server_groups WHERE group_id = ?1 AND user_id = ?2",
            nativeQuery = true)
    Optional<ServerGroup> findServerGroupByID(UUID groupID, UUID userID);



    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE server_groups SET parent_group_group_id = ?3, parent_group_user_id = ?2 WHERE group_id = ?1 AND user_id = ?2",
            nativeQuery = true)
    void addParentServerGroup(UUID childGroupID, UUID userID, UUID parentGroupID);


}
