package ru.comavp.dashboard.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "replenishment_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReplenishmentTransaction extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDate transactionDate;
    @Column
    private Double sum;
    @Column
    private Boolean nonCash;
    @Column
    private String type;
    @Column
    private String brokerName;
}
