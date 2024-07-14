package ru.comavp.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReplenishmentTransactionDto {

    private LocalDate transactionDate;
    private Double sum;
    private Boolean nonCash;
    private String type;
    private String brokerName;
}
