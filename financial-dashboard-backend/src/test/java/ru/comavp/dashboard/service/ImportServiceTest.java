package ru.comavp.dashboard.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.comavp.dashboard.ClearInMemoryDbExtension;
import ru.comavp.dashboard.utils.DataUtils;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(ClearInMemoryDbExtension.class)
public class ImportServiceTest {

    @Autowired
    private ImportService importService;
    @Autowired
    private ReplenishmentHistoryService replenishmentHistoryService;
    @Autowired
    private InvestTransactionsService investTransactionsService;

    @Test
    public void testImportAllDataFromWorkBookSheet() throws IOException {
        importService.importAllDataFromWorkBookSheet(readFileToImport());
        assertEquals(124, replenishmentHistoryService.findAll().size());
        assertEquals(299, investTransactionsService.findAll().size());
    }

    @Test
    public void testImportInvestTransactions() throws IOException {
        importService.importInvestTransactions(readFileToImport());
        assertEquals(299, investTransactionsService.findAll().size());
    }

    @Test
    public void testImportReplenishmentTransactions() throws IOException {
        importService.importReplenishmentTransactions(readFileToImport());
        assertEquals(124, replenishmentHistoryService.findAll().size());
    }

    private Workbook readFileToImport() throws IOException {
        FileInputStream file = new FileInputStream(DataUtils.FILE_PATH);
        return new XSSFWorkbook(file);
    }
}
