package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.model.dto.ReplenishmentsFilter;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;
import ru.comavp.dashboard.model.mappers.ReplenishmentMapper;
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
            return findAll(extractCurrentPage(filter));
        } else if (filter.getYear() == 0) {
            return findByBrokerName(filter.getBrokerName(), extractCurrentPage(filter));
        } if (filter.getBrokerName() == null) {
            return findAfterDate(LocalDate.of(filter.getYear(), 1, 1), extractCurrentPage(filter));
        }

        return mapper.toDtoList(replenishmentHistoryRepository.findByBrokerNameAndTransactionDateGreaterThanEqual(
                filter.getBrokerName(), LocalDate.of(filter.getYear(), 1, 1), extractCurrentPage(filter)));
    }

    public Long getReplenishmentsNumber() {
        return replenishmentHistoryRepository.count();
    }

    private Pageable extractCurrentPage(ReplenishmentsFilter filter) {
        return PageRequest.of(filter.getPageNumber(), filter.getItemsOnPage());
    }

    private List<ReplenishmentTransactionDto> findAll(Pageable pageable) {
        return mapper.toDtoList(replenishmentHistoryRepository.findAll(pageable).toList());
    }

    private List<ReplenishmentTransactionDto> findByBrokerName(String brokerName, Pageable pageable) {
        return mapper.toDtoList(replenishmentHistoryRepository.findByBrokerName(brokerName, pageable));
    }

    private List<ReplenishmentTransactionDto> findAfterDate(LocalDate date, Pageable pageable) {
        return mapper.toDtoList(replenishmentHistoryRepository.findByTransactionDateGreaterThanEqual(date, pageable));
    }
}
