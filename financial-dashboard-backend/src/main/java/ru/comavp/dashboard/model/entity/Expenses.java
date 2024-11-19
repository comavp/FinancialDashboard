package ru.comavp.dashboard.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expenses_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Expenses extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String expensesType;
    @Column
    private Double transactionValue;
    @Column
    private LocalDate transactionDate;
    @Column
    private String description;
}
