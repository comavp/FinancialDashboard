package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.dto.InvestTransactionDto;
import ru.comavp.dashboard.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.model.InvestTransaction;
import ru.comavp.dashboard.model.InvestmentPortfolioInfo;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvestTransactionsService {

    private InvestTransactionsRepository investTransactionsRepository;

    public void saveAllTransactions(List<InvestTransaction> transactionList) {
        investTransactionsRepository.saveAll(transactionList);
    }

    public List<InvestTransactionDto> findAll() {
        return investTransactionsRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<InvestTransactionDto> findByFilter(InvestTransactionsFilter filter) {
        if (filter.getYear() == 0 && filter.getBrokerName() == null) {
            return findAll();
        } else if (filter.getYear() == 0) {
            return findByBrokerName(filter.getBrokerName());
        } if (filter.getBrokerName() == null) {
            return findAfterDate(LocalDate.of(filter.getYear(), 1, 1));
        }

        return investTransactionsRepository.findByBrokerNameAndTransactionDateAfter(filter.getBrokerName(),
                        LocalDate.of(filter.getYear(), 1, 1))
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<InvestmentPortfolioInfoDto> getInvestmentPortfolioInfo() {
        return mapToDto(investTransactionsRepository.getInvestmentPortfolioInfo());
    }

    private List<InvestTransactionDto> findByBrokerName(String brokerName) {
        return investTransactionsRepository.findByBrokerName(brokerName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private List<InvestTransactionDto> findAfterDate(LocalDate date) {
        return investTransactionsRepository.findByTransactionDateAfter(date)
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    
    private InvestTransactionDto mapToDto(InvestTransaction entity) {
        return InvestTransactionDto.builder()
                .transactionDate(entity.getTransactionDate())
                .issuerName(entity.getIssuerName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .totalSum(entity.getTotalSum())
                .commission(entity.getCommission())
                .tax(entity.getTax())
                .operationType(entity.getOperationType())
                .brokerName(entity.getBrokerName())
                .build();
    }

    private List<InvestmentPortfolioInfoDto> mapToDto(List<InvestmentPortfolioInfo> entityList) {
        return entityList.stream()
                .collect(Collectors.groupingBy(InvestmentPortfolioInfo::getIssuerName))
                .entrySet()
                .stream()
                .map(entry -> InvestmentPortfolioInfoDto.builder()
                        .issueName(entry.getKey())
                        .brokerNameToQuantityMap(entry.getValue()
                                .stream()
                                .filter(item -> item.getBrokerName() != null && item.getQuantity() != null)
                                .collect(Collectors.toMap(
                                        InvestmentPortfolioInfo::getBrokerName,
                                        InvestmentPortfolioInfo::getQuantity
                                )))
                        .build())
                .toList();
    }
}
