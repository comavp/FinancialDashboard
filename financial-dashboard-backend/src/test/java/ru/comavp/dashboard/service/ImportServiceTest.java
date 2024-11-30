package ru.comavp.dashboard.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.comavp.dashboard.config.ClearInMemoryDbExtension;
import ru.comavp.dashboard.utils.DataUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(ClearInMemoryDbExtension.class)
public class ImportServiceTest {

    @Autowired
    private ImportService importService;
    @Autowired
    private ReplenishmentHistoryService replenishmentHistoryService;
    @Autowired
    private InvestTransactionsService investTransactionsService;
    @Autowired
    private IncomeHistoryService incomeHistoryService;
    @Autowired
    private ExpensesHistoryService expensesHistoryService;

    @Test
    public void testImportAllDataFromWorkBookSheet() throws IOException {
        importService.importAllDataFromWorkBookSheet(readFileToImport());
        assertEquals(124, replenishmentHistoryService.getReplenishmentsNumber());
        assertEquals(299, investTransactionsService.getInvestTransactionsNumber());
    }

    @Test
    public void testImportInvestTransactions() throws IOException {
        importService.importInvestTransactions(readFileToImport());
        assertEquals(299, investTransactionsService.getInvestTransactionsNumber());
    }

    @Test
    public void testImportReplenishmentTransactions() throws IOException {
        importService.importReplenishmentTransactions(readFileToImport());
        assertEquals(124, replenishmentHistoryService.getReplenishmentsNumber());
    }

    @Test
    public void testImportIncomeTransactionsFor2017() throws IOException {
        var workbook = readBudgetFileToImport("2017");
        importService.importIncomeTransactions(workbook);
        assertEquals(112, incomeHistoryService.getIncomeTransactionsNumber());
        assertParsedBudgetData(workbook, 1, 2, columnName -> incomeHistoryService.findSumByIncomeType(columnName));
    }

    @Test
    public void testImportExpensesTransactionsFor2017() throws IOException {
        var workbook = readBudgetFileToImport("2017");
        importService.importExpensesTransactions(workbook);
        assertEquals(484, expensesHistoryService.getExpensesTransactionsNumber());
        assertParsedBudgetData(workbook, 3, 7, columnName -> expensesHistoryService.findSumByExpensesType(columnName));
    }

    @Test
    public void testImportIncomeTransactionsFor2018() throws IOException {
        var workbook = readBudgetFileToImport("2018");
        importService.importIncomeTransactions(workbook);
        assertEquals(152, incomeHistoryService.getIncomeTransactionsNumber());
        assertParsedBudgetData(workbook, 1, 3, columnName -> incomeHistoryService.findSumByIncomeType(columnName));
    }

    @Test
    public void testImportExpensesTransactionsFor2018() throws IOException {
        var workbook = readBudgetFileToImport("2018");
        importService.importExpensesTransactions(workbook);
        assertEquals(419, expensesHistoryService.getExpensesTransactionsNumber());
        assertParsedBudgetData(workbook, 4, 8, columnName -> expensesHistoryService.findSumByExpensesType(columnName));
    }

    @Test
    public void testImportIncomeTransactionsFor2019() throws IOException {
        var workbook = readBudgetFileToImport("2019");
        importService.importIncomeTransactions(workbook);
        assertEquals(133, incomeHistoryService.getIncomeTransactionsNumber());
        assertParsedBudgetData(workbook, 1, 3, columnName -> incomeHistoryService.findSumByIncomeType(columnName));
    }

    @Test
    public void testImportExpensesTransactionsFor2019() throws IOException {
        var workbook = readBudgetFileToImport("2019");
        importService.importExpensesTransactions(workbook);
        assertEquals(730, expensesHistoryService.getExpensesTransactionsNumber());
        assertParsedBudgetData(workbook, 5, 10, columnName -> expensesHistoryService.findSumByExpensesType(columnName));
    }

    @Test
    public void testImportIncomeTransactionsFor2020() throws IOException {
        var workbook = readBudgetFileToImport("2020");
        importService.importIncomeTransactions(workbook);
        assertEquals(172, incomeHistoryService.getIncomeTransactionsNumber());
        assertParsedBudgetData(workbook, 1, 3, columnName -> incomeHistoryService.findSumByIncomeType(columnName));
    }

    @Test
    public void testImportExpensesTransactionsFor2020() throws IOException {
        var workbook = readBudgetFileToImport("2020");
        importService.importExpensesTransactions(workbook);
        assertEquals(270, expensesHistoryService.getExpensesTransactionsNumber());
        assertParsedBudgetData(workbook, 5, 11, columnName -> expensesHistoryService.findSumByExpensesType(columnName));
    }

    @Test
    public void testImportIncomeTransactionsFor2021() throws IOException {
        var workbook = readBudgetFileToImport("2021");
        importService.importIncomeTransactions(workbook);
        assertEquals(211, incomeHistoryService.getIncomeTransactionsNumber());
        assertParsedBudgetData(workbook, 1, 4, columnName -> incomeHistoryService.findSumByIncomeType(columnName));
    }

    @Test
    public void testImportExpensesTransactionsFor2021() throws IOException {
        var workbook = readBudgetFileToImport("2021");
        importService.importExpensesTransactions(workbook);
        assertEquals(565, expensesHistoryService.getExpensesTransactionsNumber());
        assertParsedBudgetData(workbook, 6, 12, columnName -> expensesHistoryService.findSumByExpensesType(columnName));
    }

    private void assertParsedBudgetData(Workbook workbook, int startColumn, int endColumn, Function<String, Double> sumExtractor) {
        var expectedResultByType = DataUtils.buildColumnNamesToTransactionsSum(workbook.getSheetAt(0), startColumn, endColumn);

        expectedResultByType.forEach((columnName, expectedValue) -> {
            Double actualValue = sumExtractor.apply(columnName);
            String errorMessage = String.format("Error during assertion of transactions sum with type '%s'. Expected sum: %f. Actual sum: %f",
                    columnName, expectedValue, actualValue);
            assertTrue(Math.abs(expectedValue - actualValue) < DataUtils.DIFF, errorMessage);
        });
    }

    private Workbook readFileToImport() throws IOException {
        FileInputStream file = new FileInputStream(DataUtils.FILE_PATH);
        return new XSSFWorkbook(file);
    }

    private Workbook readBudgetFileToImport(String year) throws IOException {
        return new XSSFWorkbook(new FileInputStream(String.format(DataUtils.BUDGET_FILE_PATH, year)));
    }
}
