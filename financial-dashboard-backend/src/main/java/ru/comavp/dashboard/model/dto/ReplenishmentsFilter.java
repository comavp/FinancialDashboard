package ru.comavp.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReplenishmentsFilter extends PageableFilter {

    private int year;
    private String brokerName;

    public ReplenishmentsFilter(String brokerName, int year, int pageNumber, int itemsOnPage) {
        super(pageNumber, itemsOnPage);
        this.year = year;
        this.brokerName = brokerName;
    }
}
