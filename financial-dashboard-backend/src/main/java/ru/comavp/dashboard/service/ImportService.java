package ru.comavp.dashboard.service;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import ru.comavp.dashboard.config.ImportProperties;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.IssuerInfo;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ImportService {

    private InvestTransactionsService investTransactionsService;
    private ReplenishmentHistoryService replenishmentHistoryService;
    private IssuerInfoService issuerInfoService;
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

    private int findStartPosition(Sheet currentSheet, String startPosition) {
        var firstRow = currentSheet.getRow(0);
        for (var currentCell : firstRow) {
            if (startPosition.equals(currentCell.getStringCellValue())) {
                return currentCell.getAddress().getColumn();
            }
        }
        return 0;
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

    private CompletableFuture<List<IssuerInfo>> extractIssuersInfo(Sheet currentSheet, int startPosition) { // todo
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
}
