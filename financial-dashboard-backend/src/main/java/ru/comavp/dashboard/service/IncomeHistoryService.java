package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.dto.IncomeDto;
import ru.comavp.dashboard.model.entity.Income;
import ru.comavp.dashboard.model.mappers.IncomeTransactionsMapper;
import ru.comavp.dashboard.repository.IncomeHistoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class IncomeHistoryService {

    private IncomeHistoryRepository incomeHistoryRepository;
    private IncomeTransactionsMapper mapper;

    public void saveAll(List<Income> incomeList) {
        incomeHistoryRepository.saveAll(incomeList);
    }

    public Iterable<IncomeDto> findAll() {
        return mapper.toDtoList(incomeHistoryRepository.findAll());
    }
}
