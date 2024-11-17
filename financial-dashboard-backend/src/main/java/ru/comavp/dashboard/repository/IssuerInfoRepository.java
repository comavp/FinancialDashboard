package ru.comavp.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.dashboard.model.entity.IssuerInfo;

public interface IssuerInfoRepository extends JpaRepository<IssuerInfo, Long> {

    boolean existsByIssuerName(String issuerName);
}
