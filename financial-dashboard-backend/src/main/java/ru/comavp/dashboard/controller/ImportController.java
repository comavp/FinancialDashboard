package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.comavp.dashboard.service.ImportService;
import ru.comavp.dashboard.utils.FileUtils;

import java.io.IOException;

@RestController
@RequestMapping("/api/import-data")
@AllArgsConstructor
@CrossOrigin
public class ImportController {

    private ImportService importService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importAllData(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importAllDataFromWorkBookSheet(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/invest-transactions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importInvestTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importInvestTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/replenishments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importReplenishments(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importReplenishmentTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/issuers-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importIssuersInfo(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importIssuersInfo(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/budget-transactions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importBudgetTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importBudgetTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/income-history", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importIncomeTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importIncomeTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/expenses-history", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importExpensesTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importExpensesTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }
}
