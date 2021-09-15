package com.team.backend.controller;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.ExpenseDetail;
import com.team.backend.model.User;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExpenseController {

    private final WalletService walletService;
    private final ExpenseService expenseService;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final UserService userService;

    public ExpenseController(WalletService walletService, ExpenseService expenseService,
                             ExpenseDetailRepository expenseDetailRepository, UserService userService) {
        this.walletService = walletService;
        this.expenseService = expenseService;
        this.expenseDetailRepository = expenseDetailRepository;
        this.userService = userService;
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
    public ResponseEntity<?> add(@PathVariable int id, @Valid @RequestBody ExpenseHolder expenseHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.save(expenseHolder, wallet);

        return new ResponseEntity<>(expenseHolder.getExpense(), HttpStatus.OK);
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody ExpenseHolder expenseHolder) {
        Expense updatedExpense = expenseService.findById(id).orElseThrow(RuntimeException::new);
        Expense newExpense = expenseHolder.getExpense();
        Wallet wallet = updatedExpense.getWallet();
        List<String> userList = expenseHolder.getUserList();
        List<String> tempList = new ArrayList<>();
        for (ExpenseDetail expenseDetail : updatedExpense.getExpenseDetailSet())
            tempList.add(expenseDetail.getUser().getLogin());

        if (!userList.equals(tempList)) {
            BigDecimal cost = updatedExpense.getTotal_cost().divide(
                    BigDecimal.valueOf(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

            expenseService.calculateNewBalance(wallet, updatedExpense, cost);

            for (String login : tempList) {
                if (!userList.contains(login)) {
                    User temp = userService.findByLogin(login).orElseThrow(RuntimeException::new);
                    ExpenseDetail expenseDetail = expenseDetailRepository.findByUser(temp).orElseThrow(RuntimeException::new);
                    updatedExpense.getExpenseDetailSet().remove(expenseDetail);
                    expenseDetailRepository.delete(expenseDetail);
                }
                userList.remove(login);
            }
            if (userList.size() != 0) {
                int size = updatedExpense.getExpenseDetailSet().size() + userList.size();
                System.out.println(size);
                cost = updatedExpense.getTotal_cost().divide(
                        BigDecimal.valueOf(size), 2, RoundingMode.CEILING);
                for (String login : userList) {
                    User member = userService.findByLogin(login).orElseThrow(RuntimeException::new);

                    ExpenseDetail expenseDetail = new ExpenseDetail();

                    expenseDetail.setCost(cost);
                    expenseDetail.setUser(member);
                    expenseDetail.setExpense(updatedExpense);

                    updatedExpense.addExpenseDetail(expenseDetail);
                    expenseDetailRepository.save(expenseDetail);
                }
                for (ExpenseDetail expenseDetail : updatedExpense.getExpenseDetailSet()) {
                    expenseDetail.setCost(cost);
                    expenseDetailRepository.save(expenseDetail);
                }
            }

            cost = updatedExpense.getTotal_cost().divide(
                    BigDecimal.valueOf(updatedExpense.getExpenseDetailSet().size()), 2, RoundingMode.CEILING)
            .multiply(BigDecimal.valueOf(-1));

            expenseService.calculateNewBalance(wallet, updatedExpense, cost);
        }

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
