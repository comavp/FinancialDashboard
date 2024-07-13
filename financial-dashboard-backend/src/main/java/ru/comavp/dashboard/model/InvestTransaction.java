package ru.comavp.dashboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "invest_transactions")
@NoArgsConstructor
@Getter
@Setter
public class InvestTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDate transactionDate;
    @Column
    private String issuerName;
    @Column
    private Integer quantity;
    @Column
    private Double price;
    @Column
    private Double totalSum;
    @Column
    private Double commission;
    @Column
    private Double tax;
    @Column
    private String operationType;
    @Column
    private String brokerName;
}
