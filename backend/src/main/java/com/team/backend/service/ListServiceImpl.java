package com.team.backend.service;

import com.team.backend.exception.StatusNotFoundException;
import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ListRepository;
import com.team.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListServiceImpl implements ListService {

    private final ListRepository listRepository;
    private final StatusRepository statusRepository;

    public ListServiceImpl(ListRepository listRepository, StatusRepository statusRepository) {
        this.listRepository = listRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void save(ListHolder listHolder, Wallet wallet) {
        Status status = statusRepository.findByName("oczekujący").orElseThrow(StatusNotFoundException::new);

        java.util.List<ListDetail> listDetailList = listHolder.getListDetailList();
        ShoppingList shoppingList = listHolder.getList();
        shoppingList.setUser(null);
        shoppingList.setWallet(wallet);
        shoppingList.setStatus(status);

        for (ListDetail listDetail : listDetailList) {
            listDetail.setUser(null);
            listDetail.setStatus(status);
            shoppingList.addListDetail(listDetail);
        }

        listRepository.save(shoppingList);
    }

    @Override
    public void save(ShoppingList list) {
        listRepository.save(list);
    }

    @Override
    public void delete(ShoppingList list) {
        listRepository.delete(list);
    }

    @Override
    public void deleteAllByWallet(Wallet wallet) {
        listRepository.deleteAllByWallet(wallet);
    }

    @Override
    public Optional<ShoppingList> findById(int id) {
        return listRepository.findById(id);
    }

    @Override
    public List<ShoppingList> findAllByWallet(Wallet wallet) {
        return listRepository.findAllByWallet(wallet);
    }

    @Override
    public List<ShoppingList> findAllByUserAndWalletAndStatus(User user, Wallet wallet, Status status) {
        return listRepository.findAllByUserAndWalletAndStatus(user, wallet, status);
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
}
