package ru.comavp.dashboard.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InvestmentPortfolioInfoDto {

    private String issueName;
    private Map<String, Long> brokerNameToQuantityMap;
}