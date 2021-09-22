package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.repository.ListRepository;
import com.team.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

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
        Status status = statusRepository.findByName("oczekujący").orElseThrow(RuntimeException::new);

        java.util.List<ListDetail> listDetailList = listHolder.getListDetailList();
        List shoppingList = listHolder.getList();
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
    public void save(List list) {
        listRepository.save(list);
    }

    @Override
    public void delete(List list) {
        listRepository.delete(list);
    }

    @Override
    public Optional<List> findById(int id) {
        return listRepository.findById(id);
    }

    @Override
    public java.util.List<List> findAllByWallet(Wallet wallet) {
        return listRepository.findAllByWallet(wallet);
    }

    @Override
    public java.util.List<List> findAllByUserAndWalletAndStatus(User user, Wallet wallet, Status status) {
        return listRepository.findAllByUserAndWalletAndStatus(user, wallet, status);
    }
}
