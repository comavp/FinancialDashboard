package ru.comavp.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestTransactionsFilter {

    private String brokerName;
    private int year;
    @Builder.Default
    private int pageNumber = 0;
    @Builder.Default
    private int itemsOnPage = 100;
}
