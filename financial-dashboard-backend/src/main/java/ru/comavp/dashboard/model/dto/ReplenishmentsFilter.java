package ru.comavp.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplenishmentsFilter {

    private int year;
    private String brokerName;
}
