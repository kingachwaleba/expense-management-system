package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.Wallet;

public interface ListService {

    void save(ListHolder listHolder, Wallet wallet);
}
