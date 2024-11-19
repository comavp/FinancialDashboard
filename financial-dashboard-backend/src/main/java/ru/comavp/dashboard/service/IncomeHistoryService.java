package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.entity.Income;
import ru.comavp.dashboard.repository.IncomeHistoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class IncomeHistoryService {

    private IncomeHistoryRepository incomeHistoryRepository;

    public void saveAll(List<Income> incomeList) {
        incomeHistoryRepository.saveAll(incomeList);
    }
}
