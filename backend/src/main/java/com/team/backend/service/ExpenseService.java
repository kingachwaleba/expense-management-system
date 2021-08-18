package com.team.backend.service;

import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Wallet;

public interface ExpenseService {

    void save(ExpenseHolder expenseHolder, Wallet wallet);
}
