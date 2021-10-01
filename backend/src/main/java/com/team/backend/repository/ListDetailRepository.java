package com.team.backend.repository;

import com.team.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListDetailRepository  extends JpaRepository<ListDetail, Integer> {

    Optional<ListDetail> findById(int id);
    java.util.List<ListDetail> findAllByUserAndListAndStatus(User user, ShoppingList list, Status status);
}
