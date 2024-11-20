package ru.comavp.dashboard.model.mappers;

import org.mapstruct.Mapper;
import ru.comavp.dashboard.model.dto.ExpensesDto;
import ru.comavp.dashboard.model.entity.Expenses;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpensesTransactionsMapper {

    Expenses toEntity(ExpensesDto expensesDto);
    ExpensesDto toDto(Expenses expenses);
    List<Expenses> toEntityList(List<ExpensesDto> incomeDtoList);
    List<ExpensesDto> toDtoList(List<Expenses> incomeList);
}
