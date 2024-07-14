package ru.comavp.dashboard.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InvestTransactionDto {

    private LocalDate transactionDate;
    private String issuerName;
    private Integer quantity;
    private Double price;
    private Double totalSum;
    private Double commission;
    private Double tax;
    private String operationType;
    private String brokerName;
}
