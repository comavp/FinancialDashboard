package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.comavp.dashboard.model.entity.Income;

public interface IncomeHistoryRepository extends JpaRepository<Income, Long> {

    @Query("SELECT SUM(i.transactionValue) FROM Income i WHERE i.incomeType = :incomeType")
    Double findTransactionsTypeByIncomeType(String incomeType);
}
