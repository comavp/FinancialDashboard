package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.InvestTransaction;

import java.time.LocalDate;
import java.util.List;

public interface InvestTransactionsRepository extends JpaRepository<InvestTransaction, Long> {

    List<InvestTransaction> findByBrokerName(String brokerName);
    List<InvestTransaction> findByTransactionDateAfter(LocalDate date);
    List<InvestTransaction> findByBrokerNameAndTransactionDateAfter(String brokerName, LocalDate date);
}
