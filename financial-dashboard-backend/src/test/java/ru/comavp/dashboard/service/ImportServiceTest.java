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
    public void testImportIncomeTransactionsFor2020() throws IOException {
        var workbook = readBudgetFileToImport("2020");
        var expectedResultByType = DataUtils.buildColumnNamesToTransactionsSum(workbook.getSheetAt(0));
        importService.importIncomeTransactions(workbook);

        assertEquals(172, incomeHistoryService.getIncomeTransactionsNumber());

        assertTrue(Math.abs(expectedResultByType.get("Стипендия") - incomeHistoryService.findSumByIncomeType("Стипендия")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Работа") - incomeHistoryService.findSumByIncomeType("Работа")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Разное") - incomeHistoryService.findSumByIncomeType("Разное")) < DataUtils.DIFF);
    }

    @Test
    public void testImportExpensesTransactionsFor2020() throws IOException {
        var workbook = readBudgetFileToImport("2020");
        var expectedResultByType = DataUtils.buildColumnNamesToTransactionsSum(workbook.getSheetAt(0));
        importService.importExpensesTransactions(workbook);

        assertEquals(270, expensesHistoryService.getExpensesTransactionsNumber());

        assertTrue(Math.abs(expectedResultByType.get("Еда") - expensesHistoryService.findSumByExpensesType("Еда")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Быт") - expensesHistoryService.findSumByExpensesType("Быт")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Здоровье") - expensesHistoryService.findSumByExpensesType("Здоровье")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Транспорт и связь") - expensesHistoryService.findSumByExpensesType("Транспорт и связь")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Разное (Расходы)") - expensesHistoryService.findSumByExpensesType("Разное")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Крупные траты") - expensesHistoryService.findSumByExpensesType("Крупные траты")) < DataUtils.DIFF);
        assertTrue(Math.abs(expectedResultByType.get("Инвестиции") - expensesHistoryService.findSumByExpensesType("Инвестиции")) < DataUtils.DIFF);
    }

    private Workbook readFileToImport() throws IOException {
        FileInputStream file = new FileInputStream(DataUtils.FILE_PATH);
        return new XSSFWorkbook(file);
    }

    private Workbook readBudgetFileToImport(String year) throws IOException {
        return new XSSFWorkbook(new FileInputStream(String.format(DataUtils.BUDGET_FILE_PATH, year)));
    }
}
