package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.dto.ExpensesDto;
import ru.comavp.dashboard.model.entity.Expenses;
import ru.comavp.dashboard.model.mappers.ExpensesTransactionsMapper;
import ru.comavp.dashboard.repository.ExpensesHistoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpensesHistoryService {

    private ExpensesHistoryRepository expensesHistoryRepository;
    private ExpensesTransactionsMapper mapper;

    public void saveAll(List<Expenses> expensesList) {
        expensesHistoryRepository.saveAll(expensesList);
    }

    public Iterable<ExpensesDto> findAll() {
        return mapper.toDtoList(expensesHistoryRepository.findAll());
    }

    public Double findSumByExpensesType(String expensesType) {
        return expensesHistoryRepository.findTransactionsTypeByExpensesType(expensesType);
    }

    public Long getExpensesTransactionsNumber() {
        return expensesHistoryRepository.count();
    }
}
