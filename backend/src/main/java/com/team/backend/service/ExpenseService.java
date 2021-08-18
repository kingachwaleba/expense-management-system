package com.team.backend.service;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.Wallet;

import java.util.List;

public interface ExpenseService {

    void save(ExpenseHolder expenseHolder, Wallet wallet);

    List<Expense> findAllByWalletOrderByDate(Wallet wallet);
}
