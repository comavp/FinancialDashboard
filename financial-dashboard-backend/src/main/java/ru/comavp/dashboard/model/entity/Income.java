package ru.comavp.dashboard.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "income_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String incomeType;
    @Column
    private Double transactionValue;
    @Column
    private LocalDate transactionDate;
    @Column
    private String description;
}
