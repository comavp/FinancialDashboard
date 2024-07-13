package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.InvestTransactions;

public interface InvestTransactionsRepository extends JpaRepository<InvestTransactions, Long> {
}
