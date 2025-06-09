package ru.comavp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, String> {
}
