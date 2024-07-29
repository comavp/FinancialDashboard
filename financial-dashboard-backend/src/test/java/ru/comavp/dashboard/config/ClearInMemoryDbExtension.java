package ru.comavp.dashboard.config;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;
import ru.comavp.dashboard.repository.ReplenishmentHistoryRepository;

public class ClearInMemoryDbExtension implements BeforeEachCallback {

    @Autowired
    private ReplenishmentHistoryRepository replenishmentHistoryRepository;
    @Autowired
    private InvestTransactionsRepository investTransactionsRepository;

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        replenishmentHistoryRepository = SpringExtension.getApplicationContext(extensionContext).getBean(ReplenishmentHistoryRepository.class);
        investTransactionsRepository = SpringExtension.getApplicationContext(extensionContext).getBean(InvestTransactionsRepository.class);
        replenishmentHistoryRepository.deleteAll();
        investTransactionsRepository.deleteAll();
    }
}
