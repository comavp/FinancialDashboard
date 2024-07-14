package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.dto.ReplenishmentsFilter;
import ru.comavp.dashboard.model.ReplenishmentTransaction;
import ru.comavp.dashboard.repository.ReplenishmentHistoryRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReplenishmentHistoryService {

    private ReplenishmentHistoryRepository replenishmentHistoryRepository;

    public void saveAllTransactions(List<ReplenishmentTransaction> transactionList) {
        replenishmentHistoryRepository.saveAll(transactionList);
    }

    public List<ReplenishmentTransactionDto> findAll() {
        return replenishmentHistoryRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ReplenishmentTransactionDto> findByFilter(ReplenishmentsFilter filter) {
        if (filter.getYear() == 0 && filter.getBrokerName() == null) {
            return findAll();
        } else if (filter.getYear() == 0) {
            return findByBrokerName(filter.getBrokerName());
        } if (filter.getBrokerName() == null) {
            return findAfterDate(LocalDate.of(filter.getYear(), 1, 1));
        }

        return replenishmentHistoryRepository.findByBrokerNameAndTransactionDateAfter(filter.getBrokerName(),
                        LocalDate.of(filter.getYear(), 1, 1))
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private List<ReplenishmentTransactionDto> findByBrokerName(String brokerName) {
        return replenishmentHistoryRepository.findByBrokerName(brokerName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private List<ReplenishmentTransactionDto> findAfterDate(LocalDate date) {
        return replenishmentHistoryRepository.findByTransactionDateAfter(date)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ReplenishmentTransactionDto mapToDto(ReplenishmentTransaction entity) {
        return ReplenishmentTransactionDto.builder()
                .transactionDate(entity.getTransactionDate())
                .sum(entity.getSum())
                .nonCash(entity.getNonCash())
                .type(entity.getType())
                .brokerName(entity.getBrokerName())
                .build();
    }
}
