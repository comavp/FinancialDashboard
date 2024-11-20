package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.comavp.dashboard.model.dto.IncomeDto;
import ru.comavp.dashboard.service.IncomeHistoryService;

@RestController
@RequestMapping("/api/income-transactions")
@AllArgsConstructor
@CrossOrigin
public class IncomeHistoryController {

    private IncomeHistoryService incomeHistoryService;

    @GetMapping
    public ResponseEntity<Iterable<IncomeDto>> findAllIncomeTransactions() {
        return ResponseEntity.ok(incomeHistoryService.findAll());
    }
}
