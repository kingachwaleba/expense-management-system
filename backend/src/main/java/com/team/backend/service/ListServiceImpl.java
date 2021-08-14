package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.List;
import com.team.backend.model.ListDetail;
import com.team.backend.model.Status;
import com.team.backend.model.Wallet;
import com.team.backend.repository.ListRepository;
import com.team.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

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
        Status status = statusRepository.findById(3).orElseThrow(RuntimeException::new);

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
}
