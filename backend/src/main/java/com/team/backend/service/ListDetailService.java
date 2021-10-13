package com.team.backend.service;

import com.team.backend.model.ListDetail;
import com.team.backend.model.ShoppingList;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface ListDetailService {

    void save(ListDetail listDetail, ShoppingList shoppingList);
    void save(ListDetail listDetail);
    void delete(ListDetail listDetail);
    Optional<ListDetail> findById(int id);
    List<ListDetail> findAllByUserAndListAndStatus(User user, ShoppingList list, Status status);
    List<String> getErrorList(BindingResult bindingResult);
}
