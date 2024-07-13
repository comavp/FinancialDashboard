package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.ReplenishmentTransaction;

public interface ReplenishmentHistoryRepository extends JpaRepository<ReplenishmentTransaction, Long> {
}
