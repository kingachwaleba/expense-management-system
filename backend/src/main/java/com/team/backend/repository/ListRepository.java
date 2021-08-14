package com.team.backend.repository;

import com.team.backend.model.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Integer> {
}
