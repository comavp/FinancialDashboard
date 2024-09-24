package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.comavp.dashboard.model.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.model.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.service.InvestTransactionsService;

@RestController
@RequestMapping("/api/invest-transactions")
@AllArgsConstructor
@CrossOrigin
public class InvestTransactionsController {

    private InvestTransactionsService investTransactionsService;

    @GetMapping
    public ResponseEntity<Iterable<InvestTransactionDto>> findAllInvestTransactions() {
        return ResponseEntity.ok(investTransactionsService.findAll());
    }

    @PostMapping("/filter")
    public ResponseEntity<Iterable<InvestTransactionDto>> findInvestTransactionsByFilter(@RequestBody InvestTransactionsFilter filter) {
        return ResponseEntity.ok(investTransactionsService.findByFilter(filter));
    }

    @GetMapping("/portfolio")
    public ResponseEntity<Iterable<InvestmentPortfolioInfoDto>> getInvestmentPortfolioInfo() {
        return ResponseEntity.ok(investTransactionsService.getInvestmentPortfolioInfo());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getInvestTransactionsNumber() {
        return ResponseEntity.ok(investTransactionsService.getInvestTransactionsNumber());
    }
}
