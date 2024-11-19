package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.entity.Income;

public interface IncomeHistoryRepository extends JpaRepository<Income, Long> {
}
