package com.team.backend.service;

import com.team.backend.exception.StatusNotFoundException;
import com.team.backend.model.ListDetail;
import com.team.backend.model.ShoppingList;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import com.team.backend.repository.ListDetailRepository;
import com.team.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListDetailServiceImpl implements ListDetailService {

    private final StatusRepository statusRepository;
    private final ListDetailRepository listDetailRepository;

    public ListDetailServiceImpl(StatusRepository statusRepository, ListDetailRepository listDetailRepository) {
        this.statusRepository = statusRepository;
        this.listDetailRepository = listDetailRepository;
    }

    @Override
    public void save(ListDetail listDetail, ShoppingList shoppingList) {
        Status status = statusRepository.findByName("oczekujący").orElseThrow(StatusNotFoundException::new);

        listDetail.setUser(null);
        listDetail.setStatus(status);
        listDetail.setList(shoppingList);
        shoppingList.addListDetail(listDetail);

        listDetailRepository.save(listDetail);
    }

    @Override
    public void save(ListDetail listDetail) {
        listDetailRepository.save(listDetail);
    }

    @Override
    public void delete(ListDetail listDetail) {
        listDetailRepository.delete(listDetail);
    }

    @Override
    public Optional<ListDetail> findById(int id) {
        return listDetailRepository.findById(id);
    }

    @Override
    public List<ListDetail> findAllByUserAndListAndStatus(User user, ShoppingList list, Status status) {
        return listDetailRepository.findAllByUserAndListAndStatus(user, list, status);
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
}
