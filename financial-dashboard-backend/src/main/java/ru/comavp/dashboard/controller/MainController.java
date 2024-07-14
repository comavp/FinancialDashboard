package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.comavp.dashboard.dto.InvestTransactionDto;
import ru.comavp.dashboard.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.dto.ReplenishmentsFilter;
import ru.comavp.dashboard.service.ImportService;
import ru.comavp.dashboard.service.InvestTransactionsService;
import ru.comavp.dashboard.service.ReplenishmentHistoryService;
import ru.comavp.dashboard.utils.FileUtils;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MainController {

    private ImportService importService;
    private ReplenishmentHistoryService replenishmentService;
    private InvestTransactionsService investTransactionsService;

    @PostMapping(value = "/import-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importData(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importInvestTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/replenishment-transactions")
    public ResponseEntity<Iterable<ReplenishmentTransactionDto>> findAllReplenishments() {
        return ResponseEntity.ok(replenishmentService.findAll());
    }

    @PostMapping("/replenishment-transactions/filter")
    public ResponseEntity<Iterable<ReplenishmentTransactionDto>> findReplenishmentsByFilter(@RequestBody ReplenishmentsFilter filter) {
        return ResponseEntity.ok(replenishmentService.findByFilter(filter));
    }

    @GetMapping("/invest-transactions")
    public ResponseEntity<Iterable<InvestTransactionDto>> findAllInvestTransactions() {
        return ResponseEntity.ok(investTransactionsService.findAll());
    }

    @PostMapping("/invest-transactions/filter")
    public ResponseEntity<Iterable<InvestTransactionDto>> findInvestTransactionsByFilter(@RequestBody InvestTransactionsFilter filter) {
        return ResponseEntity.ok(investTransactionsService.findByFilter(filter));
    }
}
