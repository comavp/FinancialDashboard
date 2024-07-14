package ru.comavp.dashboard.dto;

import lombok.Data;

@Data
public class InvestTransactionsFilter {

    private String brokerName;
    private int year;
}
