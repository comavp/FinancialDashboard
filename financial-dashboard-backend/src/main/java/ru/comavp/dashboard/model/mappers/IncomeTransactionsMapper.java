package ru.comavp.dashboard.model.mappers;

import org.mapstruct.Mapper;
import ru.comavp.dashboard.model.dto.IncomeDto;
import ru.comavp.dashboard.model.entity.Income;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IncomeTransactionsMapper {

    Income toEntity(IncomeDto incomeDto);
    IncomeDto toDto(Income income);
    List<Income> toEntityList(List<IncomeDto> incomeDtoList);
    List<IncomeDto> toDtoList(List<Income> incomeList);
}
