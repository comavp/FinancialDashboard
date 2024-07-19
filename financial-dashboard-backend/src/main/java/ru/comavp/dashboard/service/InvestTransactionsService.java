package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.model.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.model.mappers.InvestTransactionMapper;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.InvestmentPortfolioInfo;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvestTransactionsService {

    private InvestTransactionsRepository investTransactionsRepository;
    private InvestTransactionMapper mapper;

    public void saveAllTransactions(List<InvestTransaction> transactionList) {
        investTransactionsRepository.saveAll(transactionList);
    }

    public List<InvestTransactionDto> findAll() {
        return mapper.mapToDtoList(investTransactionsRepository.findAll());
    }

    public List<InvestTransactionDto> findByFilter(InvestTransactionsFilter filter) {
        if (filter.getYear() == 0 && filter.getBrokerName() == null) {
            return findAll();
        } else if (filter.getYear() == 0) {
            return findByBrokerName(filter.getBrokerName());
        } if (filter.getBrokerName() == null) {
            return findAfterDate(LocalDate.of(filter.getYear(), 1, 1));
        }

        return mapper.mapToDtoList(investTransactionsRepository.findByBrokerNameAndTransactionDateGreaterThanEqual(
                filter.getBrokerName(), LocalDate.of(filter.getYear(), 1, 1)));
    }

    public List<InvestmentPortfolioInfoDto> getInvestmentPortfolioInfo() {
        return mapToDto(investTransactionsRepository.getInvestmentPortfolioInfo());
    }

    private List<InvestTransactionDto> findByBrokerName(String brokerName) {
        return mapper.mapToDtoList(investTransactionsRepository.findByBrokerName(brokerName));
    }

    private List<InvestTransactionDto> findAfterDate(LocalDate date) {
        return mapper.mapToDtoList(investTransactionsRepository.findByTransactionDateGreaterThanEqual(date));
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
