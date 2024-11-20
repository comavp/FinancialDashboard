package ru.comavp.dashboard.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class IncomeDto {

    private LocalDate transactionDate;
    private Double transactionValue;
    private String incomeType;
    private String description;
}
