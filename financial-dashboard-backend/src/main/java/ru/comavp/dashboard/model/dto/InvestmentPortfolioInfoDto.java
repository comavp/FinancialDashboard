package ru.comavp.dashboard.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InvestmentPortfolioInfoDto {

    private String issuerName;
    private Map<String, Long> brokerNameToQuantityMap;
}
