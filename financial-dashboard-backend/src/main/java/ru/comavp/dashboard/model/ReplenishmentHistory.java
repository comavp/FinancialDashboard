package ru.comavp.dashboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "replenishment_history")
@NoArgsConstructor
@Getter
@Setter
public class ReplenishmentHistory implements Serializable {

    @Id
    @GeneratedValue
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
