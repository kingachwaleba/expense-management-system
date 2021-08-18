package com.team.backend.repository;

import com.team.backend.model.Expense;
import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findAllByWalletOrderByDate(Wallet wallet);
}
