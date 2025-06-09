package ru.comavp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
