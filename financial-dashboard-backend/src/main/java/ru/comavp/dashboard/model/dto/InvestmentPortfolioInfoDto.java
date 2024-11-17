package ru.comavp.dashboard.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InvestmentPortfolioInfoDto {

    private String ticker;
    private String issuerName;
    private String isin;
    private String category;
    private Double price;
    private Map<String, Long> brokerNameToQuantityMap;
}
