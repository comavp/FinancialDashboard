package ru.comavp.dashboard.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InvestmentPortfolioInfo {

    private String issuerName;
    private String brokerName;
    private Long quantity;
}
