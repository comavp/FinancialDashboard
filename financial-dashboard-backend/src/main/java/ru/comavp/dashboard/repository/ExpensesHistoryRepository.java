package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.entity.Expenses;

public interface ExpensesHistoryRepository extends JpaRepository<Expenses, Long> {
}
