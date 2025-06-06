package ru.comavp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Long quantity;
    @ManyToOne
    private Receipt receiptId;
}
