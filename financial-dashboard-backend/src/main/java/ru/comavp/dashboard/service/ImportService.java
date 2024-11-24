package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.config.ImportProperties;
import ru.comavp.dashboard.exceptions.CellMappingException;
import ru.comavp.dashboard.model.entity.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

@Service
@AllArgsConstructor
@Slf4j
public class ImportService {

    private InvestTransactionsService investTransactionsService;
    private ReplenishmentHistoryService replenishmentHistoryService;
    private IssuerInfoService issuerInfoService;
    private IncomeHistoryService incomeHistoryService;
    private ExpensesHistoryService expensesHistoryService;
    private ImportProperties importProperties;

    public void importAllDataFromWorkBookSheet(Workbook workbook) {
        var historySheet = workbook.getSheet(importProperties.getHistorySheetName());
        var issuersInfoSheet = workbook.getSheet(importProperties.getIssuersInfoSheetName());

        int replenishmentStartColumn = findStartPosition(historySheet, importProperties.getReplenishmentsStartPosition());
        int investmentsStartColumn = findStartPosition(historySheet, importProperties.getInvestmentsStartPosition());
        int issuersInfoStartColumn = findStartPosition(issuersInfoSheet, importProperties.getIssuersInfoSheetName());

        CompletableFuture.allOf(
                extractReplenishmentTransactions(historySheet, replenishmentStartColumn)
                        .thenCompose(transactionsList -> {
                            replenishmentHistoryService.saveAllTransactions(transactionsList);
                            return CompletableFuture.completedFuture(null);
                        }),
                extractInvestTransactions(historySheet, investmentsStartColumn)
                        .thenCompose(transactionsList -> {
                            investTransactionsService.saveAllTransactions(transactionsList);
                            return CompletableFuture.completedFuture(null);
                        }),
                extractIssuersInfo(issuersInfoSheet, issuersInfoStartColumn)
                        .thenCompose(issuerInfoList -> {
                            issuerInfoService.saveAllUniqueIssuerInfoItems(issuerInfoList);
                            return CompletableFuture.completedFuture(null);
                        })
        ).join();
    }

    public void importInvestTransactions(Workbook workbook) {
        var currentSheet = workbook.getSheet(importProperties.getHistorySheetName());
        int investmentsStarColumn = findStartPosition(currentSheet, importProperties.getInvestmentsStartPosition());
        extractInvestTransactions(currentSheet, investmentsStarColumn)
                .thenCompose(transactionsList -> {
                    investTransactionsService.saveAllTransactions(transactionsList);
                    return CompletableFuture.completedFuture(null);
                })
                .join();
    }

    public void importBudgetTransactions(Workbook workbook) {
        var currentSheet = workbook.getSheetAt(0);
        var columnIndexToColumnName = extractBudgetColumnNames(currentSheet);
        var incomeColumnRange = findBudgetColumnRange(currentSheet, importProperties.getIncomeStartPosition());
        var expensesColumnRange = findBudgetColumnRange(currentSheet, importProperties.getExpensesStartPosition());

        CompletableFuture.allOf(
                extractIncomeTransactions(currentSheet, incomeColumnRange, columnIndexToColumnName)
                        .thenCompose(transactionsList -> {
                            incomeHistoryService.saveAll(transactionsList);
                            return CompletableFuture.completedFuture(null);
                        }),
                extractExpensesTransactions(currentSheet, expensesColumnRange, columnIndexToColumnName)
                        .thenCompose(transactionsList -> {
                            expensesHistoryService.saveAll(transactionsList);
                            return CompletableFuture.completedFuture(null);
                        })
        ).join();
    }

    public void importIncomeTransactions(Workbook workbook) {
        var currentSheet = workbook.getSheetAt(0);
        var columnIndexToColumnName = extractBudgetColumnNames(currentSheet);
        var incomeColumnRange = findBudgetColumnRange(currentSheet, importProperties.getIncomeStartPosition());
        extractIncomeTransactions(currentSheet, incomeColumnRange, columnIndexToColumnName)
                .thenCompose(transactionsList -> {
                    incomeHistoryService.saveAll(transactionsList);
                    return CompletableFuture.completedFuture(null);
                })
                .join();
    }

    public void importExpensesTransactions(Workbook workbook) {
        var currentSheet = workbook.getSheetAt(0);
        var columnIndexToColumnName = extractBudgetColumnNames(currentSheet);
        var budgeColumnRange = findBudgetColumnRange(currentSheet, importProperties.getExpensesStartPosition());
        extractExpensesTransactions(currentSheet, budgeColumnRange, columnIndexToColumnName)
                .thenCompose(transactionsList -> {
                    expensesHistoryService.saveAll(transactionsList);
                    return CompletableFuture.completedFuture(null);
                })
                .join();
    }

    public void importReplenishmentTransactions(Workbook workbook) {
        var currentSheet = workbook.getSheet(importProperties.getHistorySheetName());
        int replenishmentStartColumn = findStartPosition(currentSheet, importProperties.getReplenishmentsStartPosition());
        extractReplenishmentTransactions(currentSheet, replenishmentStartColumn)
                .thenCompose(transactionsList -> {
                    replenishmentHistoryService.saveAllTransactions(transactionsList);
                    return CompletableFuture.completedFuture(null);
                })
                .join();
    }

    public void importIssuersInfo(Workbook workbook) {
        var currentSheet = workbook.getSheet(importProperties.getIssuersInfoSheetName());
        int issuerInfoStartColumn = findStartPosition(currentSheet, importProperties.getIssuersInfoStartPosition());
        extractIssuersInfo(currentSheet, issuerInfoStartColumn)
                .thenCompose(issuerInfoList -> {
                    issuerInfoService.saveAllUniqueIssuerInfoItems(issuerInfoList);
                    return CompletableFuture.completedFuture(null);
                })
                .join();
    }

    private Map<Integer, String> extractBudgetColumnNames(Sheet sheet) {
        Map<Integer, String> result = new HashMap<>();
        int ind = 1;
        var headersRow = sheet.getRow(1);
        var currentCel = headersRow.getCell(ind);
        while (!"".equals(currentCel.getStringCellValue()) || isDeficitColumn(sheet, ind)) {
            result.put(currentCel.getColumnIndex(), currentCel.getStringCellValue());
            currentCel = headersRow.getCell(++ind);
        }
        return result;
    }

    private boolean isDeficitColumn(Sheet sheet, int cellInd) {
        return "Недостача".equals(sheet.getRow(0).getCell(cellInd).getStringCellValue());
    }

    private int findStartPosition(Sheet currentSheet, String startPosition, int rowNumber) {
        var firstRow = currentSheet.getRow(rowNumber);
        for (var currentCell : firstRow) {
            if (startPosition.equals(currentCell.getStringCellValue())) {
                return currentCell.getAddress().getColumn();
            }
        }
        return 0;
    }

    private int findStartPosition(Sheet currentSheet, String startPosition) {
        return findStartPosition(currentSheet, startPosition, 0);
    }

    private BudgetColumnRange findBudgetColumnRange(Sheet sheet, String startValue) {
        int startPosition = findStartPosition(sheet, startValue);
        CellRangeAddress cellRange = findCellRangeAddressByStartPosition(sheet, startPosition);
        return new BudgetColumnRange(startPosition, cellRange.getLastColumn());
    }

    private CellRangeAddress findCellRangeAddressByStartPosition(Sheet sheet, int startPosition) {
        for (var region: sheet.getMergedRegions()) {
            if (region.isInRange(0, startPosition)) {
                return region;
            }
        }
        throw new RuntimeException("Region doesn't exist");
    }

    private CompletableFuture<List<Income>> extractIncomeTransactions(Sheet currentSheet, BudgetColumnRange budgetColumnRange,
                                                                      Map<Integer, String> columnsIndexesToNames) {
        return CompletableFuture.supplyAsync(() -> {
            var rowIterator = currentSheet.rowIterator();
            int rowsToSkip = 2;

            while(rowIterator.hasNext() && rowsToSkip > 0) {
                rowIterator.next();
                rowsToSkip--;
            }

            List<Income> resultList = new ArrayList<>();

            while(rowIterator.hasNext()) {
                var currentRow = rowIterator.next();
                if (isEndOfIncomeTransactions(currentRow)) {
                    break;
                }
                resultList.addAll(extractIncomeTransactionsFromRow(currentRow, budgetColumnRange, columnsIndexesToNames));
            }

            return resultList;
        });
    }

    private List<Income> extractIncomeTransactionsFromRow(Row currentRow, BudgetColumnRange budgetColumnRange, Map<Integer, String> columnsIndexesToNames) {
        int startPosition = budgetColumnRange.getStart();
        int endPosition = budgetColumnRange.getLength();
        var cellIterator = currentRow.cellIterator();
        LocalDate currentDate = currentRow.getCell(0)
                .getDateCellValue()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        List<Income> result = new ArrayList<>();

        while(cellIterator.hasNext() && startPosition > 1) {
            cellIterator.next();
            startPosition--;
        }

        var currentCell = cellIterator.next();

        while (cellIterator.hasNext() && currentCell.getAddress().getColumn() < endPosition) {
            currentCell = cellIterator.next();
            result.addAll(mapCellToIncomeTransaction(currentCell, columnsIndexesToNames, currentDate));
        }

        return result;
    }

    private List<Income> mapCellToIncomeTransaction(Cell currentCell, Map<Integer, String> columnsIndexesToNames, LocalDate transactionDate) {
        var incomeBuilder = Income.builder()
                .transactionDate(transactionDate)
                .incomeType(columnsIndexesToNames.get(currentCell.getColumnIndex()));
        if (currentCell.getCellType().equals(NUMERIC)) {
            if (currentCell.getNumericCellValue() == 0) return new ArrayList<>();
            else return List.of(incomeBuilder.transactionValue(currentCell.getNumericCellValue()).build());
        }
        try {
            return Stream.of(String.valueOf(currentCell.getCellFormula())
                            .replace("=", "")
                            .split("\\+"))
                    .map(currentValue -> {
                        if (currentValue.contains("-")) {
                            String[] arr = currentValue.split("-");
                            Double res = Double.parseDouble(arr[0]);
                            for (int i = 1; i < arr.length; i++) {
                                res -= Double.parseDouble(arr[i]);
                            }
                            currentValue = String.valueOf(res);
                        } else if (currentValue.contains("*")) {
                            String[] arr = currentValue.split("\\*");
                            Double res = Double.parseDouble(arr[0]);
                            for (int i = 1; i < arr.length; i++) {
                                res *= Double.parseDouble(arr[i]);
                            }
                            currentValue = String.valueOf(res);
                        }
                        return incomeBuilder.transactionValue(Double.valueOf(currentValue)).build();
                    })
                    .toList();
        } catch (Exception e) {
            String errorMessage = String.format("Error during parsing cell with rowNumber=%d and columnNumber=%d",
                    currentCell.getRowIndex() + 1, currentCell.getColumnIndex() + 1);
            log.error(errorMessage);
            throw new CellMappingException(errorMessage, e);
        }
    }

    private CompletableFuture<List<Expenses>> extractExpensesTransactions(Sheet currentSheet, BudgetColumnRange budgetColumnRange,
                                                                          Map<Integer, String> columnsIndexesToNames) {
        return CompletableFuture.supplyAsync(() -> {
            var rowIterator = currentSheet.rowIterator();
            int rowsToSkip = 2;

            while(rowIterator.hasNext() && rowsToSkip > 0) {
                rowIterator.next();
                rowsToSkip--;
            }

            List<Expenses> resultList = new ArrayList<>();

            while(rowIterator.hasNext()) {
                var currentRow = rowIterator.next();
                if (isEndOfExpensesTransactions(currentRow)) {
                    break;
                }
                resultList.addAll(extractExpensesTransactionsFromRow(currentRow, budgetColumnRange, columnsIndexesToNames));
            }

            return resultList;
        });
    }

    private List<Expenses> extractExpensesTransactionsFromRow(Row currentRow, BudgetColumnRange budgetColumnRange,
                                                              Map<Integer, String> columnsIndexesToNames) {
        int startPosition = budgetColumnRange.getStart();
        int endPosition = budgetColumnRange.getLength();
        var cellIterator = currentRow.cellIterator();
        LocalDate currentDate = currentRow.getCell(0)
                .getDateCellValue()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        List<Expenses> result = new ArrayList<>();

        while(cellIterator.hasNext() && startPosition > 0) {
            cellIterator.next();
            startPosition--;
        }

        var currentCell = cellIterator.next();
        result.addAll(mapCellToExpensesTransaction(currentCell, columnsIndexesToNames, currentDate));

        while (cellIterator.hasNext() && currentCell.getAddress().getColumn() < endPosition) {
            currentCell = cellIterator.next();
            result.addAll(mapCellToExpensesTransaction(currentCell, columnsIndexesToNames, currentDate));
        }

        return result;
    }

    private List<Expenses> mapCellToExpensesTransaction(Cell currentCell, Map<Integer, String> columnsIndexesToNames, LocalDate transactionDate) {
        var expensesBuilder = Expenses.builder()
                .transactionDate(transactionDate)
                .expensesType(columnsIndexesToNames.get(currentCell.getColumnIndex()));
        if (currentCell.getCellType().equals(NUMERIC)) {
            if (currentCell.getNumericCellValue() == 0) return new ArrayList<>();
            else return List.of(expensesBuilder.transactionValue(currentCell.getNumericCellValue()).build());
        }
        try {
            return Stream.of(String.valueOf(currentCell.getCellFormula())
                            .replace("=", "")
                            .split("\\+"))
                    .map(currentValue -> {
                        if (currentValue.contains("-")) {
                            String[] arr = currentValue.split("-");
                            Double res = Double.parseDouble(arr[0]);
                            for (int i = 1; i < arr.length; i++) {
                                res -= Double.parseDouble(arr[i]);
                            }
                            currentValue = String.valueOf(res);
                        } else if (currentValue.contains("*")) {
                            String[] arr = currentValue.split("\\*");
                            Double res = Double.parseDouble(arr[0]);
                            for (int i = 1; i < arr.length; i++) {
                                res *= Double.parseDouble(arr[i]);
                            }
                            currentValue = String.valueOf(res);
                        }
                        return expensesBuilder.transactionValue(Double.valueOf(currentValue)).build();
                    })
                    .toList();
        } catch (Exception e) {
            String errorMessage = String.format("Error during parsing cell with rowNumber=%d and columnNumber=%d",
                    currentCell.getRowIndex() + 1, currentCell.getColumnIndex() + 1);
            log.error(errorMessage);
            throw new CellMappingException(errorMessage, e);
        }
    }

    private CompletableFuture<List<ReplenishmentTransaction>> extractReplenishmentTransactions(Sheet currentSheet, int startPosition) {
        return CompletableFuture.supplyAsync(() -> {
            var rowIterator = currentSheet.rowIterator();
            int rowsToSkip = 2;

            while(rowIterator.hasNext() && rowsToSkip > 0) {
                rowIterator.next();
                rowsToSkip--;
            }

            List<ReplenishmentTransaction> resultList = new ArrayList<>();

            while(rowIterator.hasNext()) {
                var currentRow = rowIterator.next();
                if (isEndOfReplenishmentTransactions(currentRow)) {
                    break;
                }
                resultList.add(extractReplenishmentTransactionsFromRow(currentRow, startPosition));
            }

            return resultList;
        });
    }

    private boolean isEndOfReplenishmentTransactions(Row row) {
        return row.getFirstCellNum() != 0;
    }

    private boolean isEndOfIncomeTransactions(Row row) {
        return row.getFirstCellNum() != 0 || row.getCell(0).getCellType().equals(CellType.BLANK);
    }

    private boolean isEndOfExpensesTransactions(Row row) {
        return row.getFirstCellNum() != 0 || row.getCell(0).getCellType().equals(CellType.BLANK);
    }

    private boolean isEndOfInvestmentsTransactions(Row row) {
        return row.getFirstCellNum() != 6 && row.getFirstCellNum() != 0;
    }

    private boolean isEndOfIssuersInfo(Row row) {
        return "Сбербанк".equals(row.getCell(0).getStringCellValue());
    }

    private ReplenishmentTransaction extractReplenishmentTransactionsFromRow(Row currentRow, int startPosition) {
        var cellIterator = currentRow.cellIterator();
        List<Cell> cellsList = new ArrayList<>();

        while(cellIterator.hasNext() && startPosition > 1) {
            cellIterator.next();
            startPosition--;
        }

        var currentCell = cellIterator.next();
        cellsList.add(currentCell);

        while (cellIterator.hasNext() && currentCell.getAddress().getColumn() < 5) {
            currentCell = cellIterator.next();
            cellsList.add(currentCell);
        }

        return mapCellsToReplenishmentTransaction(cellsList);
    }

    private ReplenishmentTransaction mapCellsToReplenishmentTransaction(List<Cell> cellsList) {
        var entity = new ReplenishmentTransaction();
        entity.setTransactionDate(cellsList.get(0).getLocalDateTimeCellValue().toLocalDate());
        switch (cellsList.get(1).getCellType()) {
            case STRING -> entity.setSum(Double.valueOf(cellsList.get(1).getStringCellValue()));
            case NUMERIC -> entity.setSum(cellsList.get(1).getNumericCellValue());
        }
        entity.setSum(cellsList.get(1).getNumericCellValue());
        if ("безнал".equals(cellsList.get(2).getStringCellValue())) {
            entity.setNonCash(true);
        } else {
            entity.setNonCash(false);
        }
        entity.setType(cellsList.get(3).getStringCellValue());
        entity.setBrokerName(cellsList.get(4).getStringCellValue());
        return entity;
    }

    private CompletableFuture<List<InvestTransaction>> extractInvestTransactions(Sheet currentSheet, int startPosition) {
        return CompletableFuture.supplyAsync(() -> {
            var rowIterator = currentSheet.rowIterator();
            int rowsToSkip = 2;

            while(rowIterator.hasNext() && rowsToSkip > 0) {
                rowIterator.next();
                rowsToSkip--;
            }

            List<InvestTransaction> resultList = new ArrayList<>();

            while(rowIterator.hasNext()) {
                var currentRow = rowIterator.next();
                if (isEndOfInvestmentsTransactions(currentRow)) break;
                resultList.add(extractInvestTransactionsFromRow(currentRow, startPosition));
            }

            return resultList;
        });
    }

    private InvestTransaction extractInvestTransactionsFromRow(Row currentRow, int startPosition) {
        var cellIterator = currentRow.cellIterator();
        List<Cell> cellsList = new ArrayList<>();

        while(cellIterator.hasNext() && startPosition > 1 && currentRow.getFirstCellNum() == 0) {
            cellIterator.next();
            startPosition--;
        }

        var currentCell = cellIterator.next();
        cellsList.add(currentCell);

        while (cellIterator.hasNext() && currentCell.getAddress().getColumn() < 16) {
            currentCell = cellIterator.next();
            cellsList.add(currentCell);
        }

        return mapCellsToInvestTransaction(cellsList);
    }

    private InvestTransaction mapCellsToInvestTransaction(List<Cell> cellsList) {
        var entity = new InvestTransaction();
        int ind = 0;
        entity.setTransactionDate(cellsList.get(ind).getLocalDateTimeCellValue().toLocalDate());
        try {
            entity.setIssuerName(cellsList.get(++ind).getStringCellValue());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        switch (cellsList.get(++ind).getCellType()) {
            case STRING -> entity.setQuantity(Integer.valueOf(cellsList.get(ind).getStringCellValue()));
            case NUMERIC -> entity.setQuantity((int) cellsList.get(ind).getNumericCellValue());
        }
        entity.setPrice(cellsList.get(++ind).getNumericCellValue());
        if (isCellEmpty(cellsList.get(4))) {
            ind++;
        }
        entity.setTotalSum(cellsList.get(++ind).getNumericCellValue());
        entity.setCommission(cellsList.get(++ind).getNumericCellValue());
        entity.setTax(0.0);
        if (isCellEmpty(cellsList.get(7))) {
            ind++;
        }
        entity.setOperationType(cellsList.get(++ind).getStringCellValue());
        entity.setBrokerName(cellsList.get(++ind).getStringCellValue());
        return entity;
    }

    private boolean isCellEmpty(Cell cell) {
        return CellType.BLANK.equals(cell.getCellType()) && cell.getStringCellValue().isEmpty();
    }

    private CompletableFuture<List<IssuerInfo>> extractIssuersInfo(Sheet currentSheet, int startPosition) {
        return CompletableFuture.supplyAsync(() -> {
            var rowIterator = currentSheet.rowIterator();
            int rowsToSkip = 1;

            while(rowIterator.hasNext() && rowsToSkip > 0) {
                rowIterator.next();
                rowsToSkip--;
            }

            List<IssuerInfo> resultList = new ArrayList<>();

            while(rowIterator.hasNext()) {
                var currentRow = rowIterator.next();
                if (isEndOfIssuersInfo(currentRow)) {
                    break;
                }
                resultList.add(extractIssuerInfoFromRow(currentRow, startPosition));
            }

            return resultList;
        });
    }

    private IssuerInfo extractIssuerInfoFromRow(Row currentRow, int startPosition) {
        var cellIterator = currentRow.cellIterator();
        List<Cell> cellsList = new ArrayList<>();

        while(cellIterator.hasNext() && startPosition > 1 && currentRow.getFirstCellNum() == 0) {
            cellIterator.next();
            startPosition--;
        }

        var currentCell = cellIterator.next();
        cellsList.add(currentCell);

        while (cellIterator.hasNext() && currentCell.getAddress().getColumn() < 3) {
            currentCell = cellIterator.next();
            cellsList.add(currentCell);
        }

        return mapCellsIssuerInfo(cellsList);
    }

    private IssuerInfo mapCellsIssuerInfo(List<Cell> cellsList) {
        var entity = new IssuerInfo();
        int ind = 0;
        entity.setTicker(cellsList.get(ind++).getStringCellValue());
        entity.setIssuerName(cellsList.get(ind++).getStringCellValue());
        entity.setIsin(cellsList.get(ind++).getStringCellValue());
        entity.setCategory(cellsList.get(ind).getStringCellValue());
        return entity;
    }

    @Data
    @AllArgsConstructor
    public static class BudgetColumnRange {
        private int start;
        private int length;
    }
}
