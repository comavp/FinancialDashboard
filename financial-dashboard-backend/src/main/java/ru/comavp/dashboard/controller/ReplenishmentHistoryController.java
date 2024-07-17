package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.comavp.dashboard.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.dto.ReplenishmentsFilter;
import ru.comavp.dashboard.service.ReplenishmentHistoryService;

@RestController
@RequestMapping("/api/replenishment-transactions")
@AllArgsConstructor
public class ReplenishmentHistoryController {

    private ReplenishmentHistoryService replenishmentService;

    @GetMapping
    public ResponseEntity<Iterable<ReplenishmentTransactionDto>> findAllReplenishments() {
        return ResponseEntity.ok(replenishmentService.findAll());
    }

    @PostMapping("/filter")
    public ResponseEntity<Iterable<ReplenishmentTransactionDto>> findReplenishmentsByFilter(@RequestBody ReplenishmentsFilter filter) {
        return ResponseEntity.ok(replenishmentService.findByFilter(filter));
    }
}