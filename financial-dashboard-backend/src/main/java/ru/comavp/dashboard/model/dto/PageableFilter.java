package ru.comavp.dashboard.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class PageableFilter {

    @Builder.Default
    private int pageNumber = 0;
    @Builder.Default
    private int itemsOnPage = 100;
}
