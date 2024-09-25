package ru.comavp.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplenishmentsFilter extends PageableFilter {

    private int year;
    private String brokerName;
}
