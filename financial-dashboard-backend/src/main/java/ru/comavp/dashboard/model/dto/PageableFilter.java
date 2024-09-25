package ru.comavp.dashboard.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PageableFilter {

    private int pageNumber = 0;
    private int itemsOnPage = 100;
}
