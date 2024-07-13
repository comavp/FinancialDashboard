package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.InvestTransaction;

public interface InvestTransactionsRepository extends JpaRepository<InvestTransaction, Long> {
}
