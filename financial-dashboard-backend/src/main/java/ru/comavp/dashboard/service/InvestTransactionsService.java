package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.model.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.InvestmentPortfolioInfo;
import ru.comavp.dashboard.model.mappers.InvestTransactionMapper;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvestTransactionsService {

    private InvestTransactionsRepository investTransactionsRepository;
    private MoexService moexService;
    private InvestTransactionMapper mapper;

    public void saveAllTransactions(List<InvestTransaction> transactionList) {
        investTransactionsRepository.saveAll(transactionList);
    }

    public List<InvestTransactionDto> findAll() {
        return mapper.mapToDtoList(investTransactionsRepository.findAll());
    }

    public List<InvestTransactionDto> findByFilter(InvestTransactionsFilter filter) {
        if (filter.getYear() == 0 && filter.getBrokerName() == null) {
            return findAll(extractCurrentPage(filter));
        } else if (filter.getYear() == 0) {
            return findByBrokerName(filter.getBrokerName(), extractCurrentPage(filter));
        } if (filter.getBrokerName() == null) {
            return findAfterDate(LocalDate.of(filter.getYear(), 1, 1), extractCurrentPage(filter));
        }

        return mapper.mapToDtoList(investTransactionsRepository.findByBrokerNameAndTransactionDateGreaterThanEqual(
                filter.getBrokerName(), LocalDate.of(filter.getYear(), 1, 1), extractCurrentPage(filter)));
    }

    public List<InvestmentPortfolioInfoDto> getInvestmentPortfolioInfo() {
        return mapToDto(investTransactionsRepository.getInvestmentPortfolioInfo());
    }

    public Long getInvestTransactionsNumber() {
        return investTransactionsRepository.count();
    }

    private Pageable extractCurrentPage(InvestTransactionsFilter filter) {
        return PageRequest.of(filter.getPageNumber(), filter.getItemsOnPage());
    }

    private List<InvestTransactionDto> findAll(Pageable pageable) {
        return mapper.mapToDtoList(investTransactionsRepository.findAll(pageable).toList());
    }

    private List<InvestTransactionDto> findByBrokerName(String brokerName, Pageable pageable) {
        return mapper.mapToDtoList(investTransactionsRepository.findByBrokerName(brokerName, pageable));
    }

    private List<InvestTransactionDto> findAfterDate(LocalDate date, Pageable pageable) {
        return mapper.mapToDtoList(investTransactionsRepository.findByTransactionDateGreaterThanEqual(date, pageable));
    }

    private List<InvestmentPortfolioInfoDto> mapToDto(List<InvestmentPortfolioInfo> entityList) {
        return entityList.stream()
                .collect(Collectors.groupingBy(InvestmentPortfolioInfo::getIssuerName))
                .entrySet()
                .stream()
                .map(entry -> InvestmentPortfolioInfoDto.builder()
                        .issuerName(entry.getKey())
                        .price(moexService.getPriceByIssuerNameAndDate(entry.getKey(), LocalDate.now()))
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
