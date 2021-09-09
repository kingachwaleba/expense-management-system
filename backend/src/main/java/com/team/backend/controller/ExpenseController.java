package com.team.backend.controller;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.ExpenseDetail;
import com.team.backend.model.Wallet;
import com.team.backend.model.WalletUser;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExpenseController {

    private final WalletService walletService;
    private final ExpenseService expenseService;

    public ExpenseController(WalletService walletService, ExpenseService expenseService) {
        this.walletService = walletService;
        this.expenseService = expenseService;
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(expense, HttpStatus.OK);
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

    @PutMapping("/expense/{id}")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody Expense newExpense) {
        Expense updatedExpense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        updatedExpense.setName(newExpense.getName());
        updatedExpense.setTotal_cost(newExpense.getTotal_cost());

        for (ExpenseDetail expenseDetail : updatedExpense.getExpenseDetailSet()) {
            BigDecimal cost = updatedExpense.getTotal_cost().divide(BigDecimal.valueOf(updatedExpense
                    .getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

            expenseDetail.setCost(cost);
        }

        updatedExpense.setCategory(newExpense.getCategory());
        updatedExpense.setPeriod(newExpense.getPeriod());

        expenseService.save(updatedExpense);

        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.delete(expense);

        return new ResponseEntity<>("The given expense was deleted!", HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/balance")
    public ResponseEntity<?> getWalletBalance(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
        Map<Integer, BigDecimal> balanceMap = new HashMap<>();
        walletUserList.forEach(walletUser -> balanceMap.put(walletUser.getUser().getId(), walletUser.getBalance()));
        walletService.simplifyDebts(balanceMap);

        return new ResponseEntity<>("Aaaa", HttpStatus.OK);
    }
}
