package ru.comavp.dashboard.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "replenishment_history")
@NoArgsConstructor
@Getter
@Setter
public class ReplenishmentTransaction implements Serializable {

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
