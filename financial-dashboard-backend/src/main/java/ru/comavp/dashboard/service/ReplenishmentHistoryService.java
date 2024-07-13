package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.ReplenishmentTransaction;
import ru.comavp.dashboard.repository.ReplenishmentHistoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ReplenishmentHistoryService {

    private ReplenishmentHistoryRepository replenishmentHistoryRepository;

    public void saveAllTransactions(List<ReplenishmentTransaction> transactionList) {
        replenishmentHistoryRepository.saveAll(transactionList);
    }
}
