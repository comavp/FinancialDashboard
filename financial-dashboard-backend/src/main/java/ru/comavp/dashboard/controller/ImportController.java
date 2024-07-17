package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.comavp.dashboard.service.ImportService;
import ru.comavp.dashboard.utils.FileUtils;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ImportController {

    private ImportService importService;

    @PostMapping(value = "/import-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importAllData(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importAllDataFromWorkBookSheet(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/import-data/invest-transactions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importInvestTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importInvestTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/import-data/replenishments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importReplenishments(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importReplenishmentTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }
}
