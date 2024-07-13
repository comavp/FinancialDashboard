package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.ReplenishmentHistory;

public interface ReplenishmentHistoryRepository extends JpaRepository<ReplenishmentHistory, Long> {
}
