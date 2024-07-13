package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.model.InvestTransaction;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class InvestTransactionsService {

    private InvestTransactionsRepository investTransactionsRepository;

    public void saveAllTransactions(List<InvestTransaction> transactionList) {
        investTransactionsRepository.saveAll(transactionList);
    }
}
