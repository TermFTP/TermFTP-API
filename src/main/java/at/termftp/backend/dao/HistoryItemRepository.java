package at.termftp.backend.dao;

import at.termftp.backend.model.HistoryItem;
import at.termftp.backend.model.HistoryItemID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface HistoryItemRepository extends JpaRepository<HistoryItem, HistoryItemID> {
    Optional<HistoryItem> findHistoryItemByUser(UUID userID);
    Optional<List<HistoryItem>> findHistoryItemsByUser(UUID userID);
}
