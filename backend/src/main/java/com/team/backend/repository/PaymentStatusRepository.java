package com.team.backend.repository;

import com.team.backend.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {

    Optional<PaymentStatus> findByName(String name);
}
