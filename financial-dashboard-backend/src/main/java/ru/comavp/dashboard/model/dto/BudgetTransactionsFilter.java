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
public class BudgetTransactionsFilter extends PageableFilter {

    private int year;
    private int month;
    private String category;
}
