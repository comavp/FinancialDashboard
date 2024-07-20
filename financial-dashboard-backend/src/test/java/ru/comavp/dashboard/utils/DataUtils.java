package ru.comavp.dashboard.utils;

import lombok.experimental.UtilityClass;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;

import java.time.LocalDate;

@UtilityClass
public class DataUtils {

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
}
