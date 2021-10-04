package com.team.backend.controller;

import com.team.backend.exception.ExpenseNotFoundException;
import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.Wallet;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;
    private final MessageService messageService;

    public ExpenseController(ExpenseService expenseService, MessageService messageService) {
        this.expenseService = expenseService;
        this.messageService = messageService;
    }

    @GetMapping("/expense/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByExpense(#id)")
    public ResponseEntity<?> one(@PathVariable int id) {
        return new ResponseEntity<>(expenseService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/expenses")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> all(@PathVariable int id) {
        return new ResponseEntity<>(expenseService.getAll(id), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/add-expense")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> add(@PathVariable int id, @Valid @RequestBody ExpenseHolder expenseHolder) {
        expenseService.save(expenseHolder, id);
        messageService.sendNotification(id);

        return new ResponseEntity<>(expenseHolder.getExpense(), HttpStatus.OK);
    }

    @PutMapping("/expense/{id}")
    @PreAuthorize("@authenticationService.ifExpenseOwner(#id) && !@authenticationService.ifContainsDeletedMembers(#id)")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody ExpenseHolder expenseHolder) {
        Expense updatedExpense = expenseService.findById(id).orElseThrow(ExpenseNotFoundException::new);
        Expense newExpense = expenseHolder.getExpense();
        List<String> userList = expenseHolder.getUserList();

        expenseService.editUserList(updatedExpense, newExpense, userList);
        expenseService.edit(updatedExpense, newExpense);
        messageService.sendNotification(updatedExpense.getWallet().getId());

        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/expense/{id}")
    @PreAuthorize("@authenticationService.ifExpenseOwner(#id) && !@authenticationService.ifContainsDeletedMembers(#id)")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(ExpenseNotFoundException::new);
        int walletId = expense.getWallet().getId();
        expenseService.deleteExpense(expense);
        messageService.sendNotification(walletId);

        return new ResponseEntity<>("The given expense was deleted!", HttpStatus.OK);
    }
}
