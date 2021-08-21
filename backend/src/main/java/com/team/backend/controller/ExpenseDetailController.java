package com.team.backend.controller;

import com.team.backend.model.Expense;
import com.team.backend.model.ExpenseDetail;
import com.team.backend.model.PaymentStatus;
import com.team.backend.model.User;
import com.team.backend.repository.ExpenseDetailRepository;
import com.team.backend.repository.PaymentStatusRepository;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
public class ExpenseDetailController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final PaymentStatusRepository paymentStatusRepository;
    private final ExpenseDetailRepository expenseDetailRepository;

    public ExpenseDetailController(ExpenseService expenseService, UserService userService,
                                   PaymentStatusRepository paymentStatusRepository, ExpenseDetailRepository expenseDetailRepository) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.paymentStatusRepository = paymentStatusRepository;
        this.expenseDetailRepository = expenseDetailRepository;
    }

    @Transactional
    @PutMapping("/expense/{id}/add-user/{login}")
    public ResponseEntity<?> addUser(@PathVariable int id, @PathVariable String login) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);
        User user = userService.findByLogin(login).orElseThrow(RuntimeException::new);
        PaymentStatus waitingStatus = paymentStatusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);

        BigDecimal cost = expense.getTotal_cost().divide(BigDecimal.valueOf(expense
                .getExpenseDetailSet().size() + 1), 2, RoundingMode.CEILING);

        ExpenseDetail expenseDetail = new ExpenseDetail();
        expenseDetail.setCost(cost);
        expenseDetail.setUser(user);
        expenseDetail.setExpense(expense);
        expenseDetail.setPaymentStatus(waitingStatus);

        expenseDetailRepository.save(expenseDetail);

        expense.addExpenseDetail(expenseDetail);

        for (ExpenseDetail expenseDetail1 : expense.getExpenseDetailSet())
            expenseDetail1.setCost(cost);

        expenseService.save(expense);

        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @DeleteMapping("/expense/{id}/delete-user")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        ExpenseDetail deletedUser = expenseDetailRepository.findById(id).orElseThrow(RuntimeException::new);
        Expense expense = deletedUser.getExpense();

        expenseDetailRepository.delete(deletedUser);

        return new ResponseEntity<>(expense, HttpStatus.OK);
    }
}
