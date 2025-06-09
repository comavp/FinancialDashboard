package ru.comavp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
