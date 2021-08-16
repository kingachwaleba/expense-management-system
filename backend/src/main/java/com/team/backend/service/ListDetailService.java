package com.team.backend.service;

import com.team.backend.model.List;
import com.team.backend.model.ListDetail;

import java.util.Optional;

public interface ListDetailService {

    void save(ListDetail listDetail, List shoppingList);
    void save(ListDetail listDetail);
    void delete(ListDetail listDetail);
    Optional<ListDetail> findById(int id);
}
