package com.team.backend.controller;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ExpenseDetailRepository;
import com.team.backend.repository.WalletUserRepository;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class ExpenseController {

    private final WalletService walletService;
    private final ExpenseService expenseService;
    private final WalletUserRepository walletUserRepository;
    private final ExpenseDetailRepository expenseDetailRepository;

    public ExpenseController(WalletService walletService, ExpenseService expenseService, WalletUserRepository walletUserRepository, ExpenseDetailRepository expenseDetailRepository) {
        this.walletService = walletService;
        this.expenseService = expenseService;
        this.walletUserRepository = walletUserRepository;
        this.expenseDetailRepository = expenseDetailRepository;
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

        BigDecimal oldCost = updatedExpense.getTotal_cost();
        BigDecimal newCost = newExpense.getTotal_cost();
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

        if (oldCost.compareTo(newCost) != 0) {
            Wallet wallet = updatedExpense.getWallet();
            User owner = updatedExpense.getUser();
            List<WalletUser> walletUserList = walletService.findWalletUserList(wallet);
            BigDecimal cost = oldCost.subtract(newCost).divide(
                    BigDecimal.valueOf(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING);
            WalletUser ownerDetails = walletUserList.stream()
                    .filter(temp -> temp.getUser().equals(owner)).findAny().orElseThrow(RuntimeException::new);

            walletUserList.stream().filter(walletUser -> !walletUser.equals(ownerDetails))
                    .forEach(wu -> {
                        BigDecimal oldBalance = wu.getBalance();
                        wu.setBalance(oldBalance.add(cost));
                        walletUserRepository.save(wu);

                        oldBalance = ownerDetails.getBalance();
                        ownerDetails.setBalance(oldBalance.subtract(cost));
                        walletUserRepository.save(ownerDetails);
                    });
        }

        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.delete(expense);

        return new ResponseEntity<>("The given expense was deleted!", HttpStatus.OK);
    }
}
