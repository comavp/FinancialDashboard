package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.comavp.dashboard.model.dto.ExpensesDto;
import ru.comavp.dashboard.service.ExpensesHistoryService;

@RestController
@RequestMapping("/api/expenses-transactions")
@AllArgsConstructor
@CrossOrigin
public class ExpensesHistoryController {

    private ExpensesHistoryService expensesHistoryService;

    @GetMapping
    public ResponseEntity<Iterable<ExpensesDto>> findAllExpensesTransactions() {
        return ResponseEntity.ok(expensesHistoryService.findAll());
    }
}
