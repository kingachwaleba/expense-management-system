package com.team.backend.controller;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.Wallet;
import com.team.backend.repository.ExpenseDetailRepository;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        List<String> deletedUserList = walletService.findDeletedUserList(expense.getWallet());
        Map<String, Object> map = new HashMap<>();
        map.put("expense", expense);
        map.put("deletedUserList", deletedUserList);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/expenses")
    public ResponseEntity<?> all(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        List<String> deletedUserList = walletService.findDeletedUserList(wallet);
        Map<String, Object> map = new HashMap<>();
        map.put("allExpenses", expenseService.findAllByWalletOrderByDate(wallet));
        map.put("deletedUserList", deletedUserList);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/add-expense")
    public ResponseEntity<?> add(@PathVariable int id, @Valid @RequestBody ExpenseHolder expenseHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.save(expenseHolder, wallet);

        return new ResponseEntity<>(expenseHolder.getExpense(), HttpStatus.OK);
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody ExpenseHolder expenseHolder) {
        Expense updatedExpense = expenseService.findById(id).orElseThrow(RuntimeException::new);
        Expense newExpense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();

        expenseService.editUserList(updatedExpense, newExpense, userList);
        expenseService.edit(updatedExpense, newExpense);

        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);
        expenseService.deleteExpense(expense);

        return new ResponseEntity<>("The given expense was deleted!", HttpStatus.OK);
    }
}
