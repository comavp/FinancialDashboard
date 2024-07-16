package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.dto.ReplenishmentsFilter;
import ru.comavp.dashboard.mappers.ReplenishmentMapper;
import ru.comavp.dashboard.model.ReplenishmentTransaction;
import ru.comavp.dashboard.repository.ReplenishmentHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReplenishmentHistoryService {

    private ReplenishmentHistoryRepository replenishmentHistoryRepository;
    private ReplenishmentMapper mapper;

    public void saveAllTransactions(List<ReplenishmentTransaction> transactionList) {
        replenishmentHistoryRepository.saveAll(transactionList);
    }

    public List<ReplenishmentTransactionDto> findAll() {
        return mapper.toDtoList(replenishmentHistoryRepository.findAll());
    }

    public List<ReplenishmentTransactionDto> findByFilter(ReplenishmentsFilter filter) {
        if (filter.getYear() == 0 && filter.getBrokerName() == null) {
            return findAll();
        } else if (filter.getYear() == 0) {
            return findByBrokerName(filter.getBrokerName());
        } if (filter.getBrokerName() == null) {
            return findAfterDate(LocalDate.of(filter.getYear(), 1, 1));
        }

        return mapper.toDtoList(replenishmentHistoryRepository.findByBrokerNameAndTransactionDateAfter(
                filter.getBrokerName(), LocalDate.of(filter.getYear(), 1, 1)));
    }

    private List<ReplenishmentTransactionDto> findByBrokerName(String brokerName) {
        return mapper.toDtoList(replenishmentHistoryRepository.findByBrokerName(brokerName));
    }

    private List<ReplenishmentTransactionDto> findAfterDate(LocalDate date) {
        return mapper.toDtoList(replenishmentHistoryRepository.findByTransactionDateAfter(date));
    }
}
