package ru.comavp.dashboard.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "invest_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvestTransaction extends AbstractEntity {

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
