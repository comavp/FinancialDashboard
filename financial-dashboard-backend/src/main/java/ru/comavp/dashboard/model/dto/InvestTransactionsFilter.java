package ru.comavp.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InvestTransactionsFilter extends PageableFilter {

    private String brokerName;
    private int year;

    public InvestTransactionsFilter(String brokerName, int year, int pageNumber, int itemsOnPage) {
        super(pageNumber, itemsOnPage);
        this.brokerName = brokerName;
        this.year = year;
    }
}
