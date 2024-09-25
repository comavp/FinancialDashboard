package ru.comavp.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestTransactionsFilter extends PageableFilter {

    private String brokerName;
    private int year;
}
