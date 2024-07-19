package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;

import java.time.LocalDate;
import java.util.List;

public interface ReplenishmentHistoryRepository extends JpaRepository<ReplenishmentTransaction, Long> {

    List<ReplenishmentTransaction> findByBrokerName(String brokerName);
    List<ReplenishmentTransaction> findByTransactionDateGreaterThanEqual(LocalDate date);
    List<ReplenishmentTransaction> findByBrokerNameAndTransactionDateGreaterThanEqual(String brokerName, LocalDate date);
}
