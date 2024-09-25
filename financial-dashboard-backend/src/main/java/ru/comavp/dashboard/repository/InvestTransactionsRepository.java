package ru.comavp.dashboard.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.InvestmentPortfolioInfo;

import java.time.LocalDate;
import java.util.List;

public interface InvestTransactionsRepository extends JpaRepository<InvestTransaction, Long> {

    List<InvestTransaction> findByBrokerName(String brokerName, Pageable pageable);
    List<InvestTransaction> findByTransactionDateGreaterThanEqual(LocalDate date, Pageable pageable);
    List<InvestTransaction> findByBrokerNameAndTransactionDateGreaterThanEqual(String brokerName, LocalDate date, Pageable pageable);
    @Query("SELECT new ru.comavp.dashboard.model.entity.InvestmentPortfolioInfo(it.issuerName, it.brokerName, SUM(it.quantity)) " +
            "FROM InvestTransaction AS it GROUP BY it.issuerName, it.brokerName")
    List<InvestmentPortfolioInfo> getInvestmentPortfolioInfo();
}
