package ru.comavp.dashboard.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.comavp.dashboard.model.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.model.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@UtilityClass
public class DataUtils {

    public String FILE_PATH = "src\\test\\resources\\test_file.xlsx";
    public String BUDGET_FILE_PATH = "src\\test\\resources\\%s.xlsx";
    public Double DIFF = 0.001;

    public InvestTransaction generateInvestTransaction(String issuerName, LocalDate transactionDate, String brokerName) {
        return InvestTransaction.builder()
                .transactionDate(transactionDate)
                .issuerName(issuerName)
                .quantity(1)
                .price(0.0)
                .totalSum(0.0)
                .commission(0.0)
                .tax(0.0)
                .operationType("test")
                .brokerName(brokerName)
                .build();
    }

    public ReplenishmentTransaction generateReplenishmentTransaction(LocalDate transactionDate, String brokerName) {
        return ReplenishmentTransaction.builder()
                .transactionDate(transactionDate)
                .sum(0.0)
                .nonCash(true)
                .type("test")
                .brokerName(brokerName)
                .build();
    }

    public InvestTransactionDto generateInvestTransactionDto() {
        return InvestTransactionDto.builder()
                .transactionDate(LocalDate.now())
                .issuerName("test")
                .quantity(1)
                .price(1.0)
                .totalSum(1.0)
                .commission(0.0)
                .tax(0.0)
                .operationType("test")
                .brokerName("test")
                .build();
    }

    public ReplenishmentTransactionDto generateReplenishmentTransactionDto() {
        return ReplenishmentTransactionDto.builder()
                .transactionDate(LocalDate.now())
                .sum(10.0)
                .nonCash(true)
                .type("test")
                .brokerName("test")
                .build();
    }

    public InvestmentPortfolioInfoDto generateInvestmentPortfolioDto() {
        return InvestmentPortfolioInfoDto.builder()
                .issuerName("test")
                .brokerNameToQuantityMap(Map.of(
                        "testBroker1", 1L,
                        "testBroker2", 2L
                ))
                .build();
    }

    public Map<String, Double> buildColumnNamesToTransactionsSum(Sheet sheet) {
        int startRow = 1;
        int startColumn = 1;
        String startColumnName = sheet.getRow(startRow).getCell(startColumn).getStringCellValue();
        Map<String, Double> columnNamesToTransactionsSum = new LinkedHashMap<>();
        Row row = null;
        for (var rowIt = sheet.rowIterator(); rowIt.hasNext();) {
            Row currentRow = rowIt.next();
            Cell currentCell = currentRow.getCell(1);
            if (currentCell.getCellType().equals(STRING) && startColumnName.equals(currentCell.getStringCellValue()) && currentCell.getRowIndex() != startRow) {
                row = currentRow;
                break;
            }
        }
        for (var cellIt = row.cellIterator(); cellIt.hasNext(); ) {
            Cell columnNameCell = cellIt.next();
            Cell valueCell = sheet.getRow(columnNameCell.getRowIndex() + 1).getCell(columnNameCell.getColumnIndex());
            String columnName = columnNameCell.getStringCellValue();
            if (columnNamesToTransactionsSum.containsKey(columnName)) {
                columnName += " (Расходы)";
            }
            columnNamesToTransactionsSum.put(columnName, valueCell.getNumericCellValue());
        }
        return columnNamesToTransactionsSum;
    }
}
