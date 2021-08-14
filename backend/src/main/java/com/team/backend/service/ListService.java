package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.List;
import com.team.backend.model.Wallet;

import java.util.Map;
import java.util.Optional;

public interface ListService {

    void save(ListHolder listHolder, Wallet wallet);
    void save(List list);

    Optional<List> findById(int id);
}
