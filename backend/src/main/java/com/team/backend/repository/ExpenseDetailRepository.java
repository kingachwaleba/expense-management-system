package com.team.backend.repository;

import com.team.backend.model.ExpenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Integer> {
}
