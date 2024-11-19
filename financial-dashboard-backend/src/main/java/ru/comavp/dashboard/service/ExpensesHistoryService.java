package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.entity.Expenses;
import ru.comavp.dashboard.repository.ExpensesHistoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpensesHistoryService {

    private ExpensesHistoryRepository expensesHistoryRepository;

    public void saveAll(List<Expenses> expensesList) {
        expensesHistoryRepository.saveAll(expensesList);
    }
}
