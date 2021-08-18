package com.team.backend.controller;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ExpenseController {

    private final WalletService walletService;
    private final ExpenseService expenseService;

    public ExpenseController(WalletService walletService, ExpenseService expenseService) {
        this.walletService = walletService;
        this.expenseService = expenseService;
    }

    @GetMapping("/wallet/{id}/expenses")
    public ResponseEntity<?> all(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(expenseService.findAllByWalletOrderByDate(wallet), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/add-expense")
    public ResponseEntity<?> addExpense(@PathVariable int id, @Valid @RequestBody ExpenseHolder expenseHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.save(expenseHolder, wallet);

        return new ResponseEntity<>(expenseHolder.getExpense(), HttpStatus.OK);
    }
}
