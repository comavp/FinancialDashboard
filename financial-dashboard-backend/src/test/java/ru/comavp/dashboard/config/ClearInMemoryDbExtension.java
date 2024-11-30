package ru.comavp.dashboard.config;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.comavp.dashboard.repository.ExpensesHistoryRepository;
import ru.comavp.dashboard.repository.IncomeHistoryRepository;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;
import ru.comavp.dashboard.repository.ReplenishmentHistoryRepository;

public class ClearInMemoryDbExtension implements BeforeEachCallback {

    @Autowired
    private ReplenishmentHistoryRepository replenishmentHistoryRepository;
    @Autowired
    private InvestTransactionsRepository investTransactionsRepository;
    @Autowired
    private IncomeHistoryRepository incomeHistoryRepository;
    @Autowired
    private ExpensesHistoryRepository expensesHistoryRepository;

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        replenishmentHistoryRepository = SpringExtension.getApplicationContext(extensionContext).getBean(ReplenishmentHistoryRepository.class);
        investTransactionsRepository = SpringExtension.getApplicationContext(extensionContext).getBean(InvestTransactionsRepository.class);
        incomeHistoryRepository = SpringExtension.getApplicationContext(extensionContext).getBean(IncomeHistoryRepository.class);
        expensesHistoryRepository = SpringExtension.getApplicationContext(extensionContext).getBean(ExpensesHistoryRepository.class);

        replenishmentHistoryRepository.deleteAll();
        investTransactionsRepository.deleteAll();
        incomeHistoryRepository.deleteAll();
        expensesHistoryRepository.deleteAll();
    }
}
