package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.comavp.dashboard.model.entity.Expenses;

import java.util.Optional;

public interface ExpensesHistoryRepository extends JpaRepository<Expenses, Long> {

    @Query("SELECT SUM(e.transactionValue) FROM Expenses e WHERE  e.expensesType = :expensesType")
    Optional<Double> findTransactionsTypeByExpensesType(String expensesType);
}
