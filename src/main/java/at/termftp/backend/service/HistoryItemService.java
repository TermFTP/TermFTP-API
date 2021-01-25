package at.termftp.backend.service;

import at.termftp.backend.dao.HistoryItemRepository;
import at.termftp.backend.model.HistoryItem;
import at.termftp.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoryItemService {
    private final HistoryItemRepository historyItemRepository;
    private final UserService userService;

    public HistoryItemService(HistoryItemRepository historyItemRepository, UserService userService) {
        this.historyItemRepository = historyItemRepository;
        this.userService = userService;
    }

    /**
     * used to save a HistoryItem
     * @return HistoryItem
     */
    public HistoryItem saveHistoryItem(HistoryItem historyItem) {
        return historyItemRepository.save(historyItem);
    }

    public List<HistoryItem> getHistoryItemsByUser(User user){
        return historyItemRepository.findHistoryItemsByUser(user).orElse(null);
    }
}
