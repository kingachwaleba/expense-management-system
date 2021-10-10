package com.team.backend.repository;

import com.team.backend.model.Expense;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    Optional<Expense> findById(int id);
    List<Expense> findAllByWalletOrderByDate(Wallet wallet);
    List<Expense> findAllByWalletAndUser(Wallet wallet, User user);
    List<Expense> findAllByWalletAndDateBetween(Wallet wallet, LocalDateTime dateFrom, LocalDateTime dateTo);
    void deleteAllByWallet(Wallet wallet);
}
