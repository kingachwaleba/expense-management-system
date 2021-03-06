package com.team.backend.repository;

import com.team.backend.model.Expense;
import com.team.backend.model.ExpenseDetail;
import com.team.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Integer> {

    Optional<ExpenseDetail> findByUserAndExpense(User user, Expense expense);
}
