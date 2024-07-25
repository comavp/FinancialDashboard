package ru.comavp.dashboard.utils;

import lombok.experimental.UtilityClass;
import ru.comavp.dashboard.model.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.model.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;

import java.time.LocalDate;
import java.util.Map;

@UtilityClass
public class DataUtils {

    public String FILE_PATH = "src\\test\\resources\\test_file.xlsx";

    public InvestTransaction generateInvestTransaction(String issuerName, LocalDate transactionDate, String brokerName) {
        return InvestTransaction.builder()
                .transactionDate(transactionDate)
                .issuerName(issuerName)
                .quantity(1)
                .price(0.0)
                .totalSum(0.0)
                .commission(0.0)
                .tax(0.0)
                .operationType("test")
                .brokerName(brokerName)
                .build();
    }

    public ReplenishmentTransaction generateReplenishmentTransaction(LocalDate transactionDate, String brokerName) {
        return ReplenishmentTransaction.builder()
                .transactionDate(transactionDate)
                .sum(0.0)
                .nonCash(true)
                .type("test")
                .brokerName(brokerName)
                .build();
    }

    public InvestTransactionDto generateInvestTransactionDto() {
        return InvestTransactionDto.builder()
                .transactionDate(LocalDate.now())
                .issuerName("test")
                .quantity(1)
                .price(1.0)
                .totalSum(1.0)
                .commission(0.0)
                .tax(0.0)
                .operationType("test")
                .brokerName("test")
                .build();
    }

    public ReplenishmentTransactionDto generateReplenishmentTransactionDto() {
        return ReplenishmentTransactionDto.builder()
                .transactionDate(LocalDate.now())
                .sum(10.0)
                .nonCash(true)
                .type("test")
                .brokerName("test")
                .build();
    }

    public InvestmentPortfolioInfoDto generateInvestmentPortfolioDto() {
        return InvestmentPortfolioInfoDto.builder()
                .issuerName("test")
                .brokerNameToQuantityMap(Map.of(
                        "testBroker1", 1L,
                        "testBroker2", 2L
                ))
                .build();
    }
}
