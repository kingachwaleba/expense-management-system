package com.team.backend.service;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.List;
import com.team.backend.model.ListDetail;
import com.team.backend.model.Status;
import com.team.backend.model.Wallet;
import com.team.backend.repository.ListRepository;
import com.team.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    @Override
    public Map<String, Object> showListDetails(List shoppingList) {
        Map<String, Object> map = new HashMap<>();

        map.put("shoppingListId", shoppingList.getId());
        map.put("shoppingListName", shoppingList.getName());
        map.put("shoppingListStatus", shoppingList.getStatus());

        if (shoppingList.getUser() != null)
            map.put("shoppingListUser", shoppingList.getUser().getLogin());
        else
            map.put("shoppingListUser", shoppingList.getUser());

        map.put("walletId", shoppingList.getWallet().getId());

        java.util.List<Map<String, Object>> listDetailList = new ArrayList<>();

        for (ListDetail listDetail : shoppingList.getListDetailSet()) {
            Map<String, Object> listDetailMap = new HashMap<>();

            listDetailMap.put("listDetailId", listDetail.getId());
            listDetailMap.put("listDetailName", listDetail.getName());
            listDetailMap.put("quantity", listDetail.getQuantity());
            listDetailMap.put("unit", listDetail.getUnit().getName());
            listDetailMap.put("listDetailStatus", listDetail.getStatus());

            if (shoppingList.getUser() != null)
                map.put("shoppingListUser", shoppingList.getUser().getLogin());
            else
                map.put("shoppingListUser", shoppingList.getUser());

            listDetailList.add(listDetailMap);
        }

        map.put("listDetailMap", listDetailList);

        return map;
    }

    @Override
    public Optional<List> findById(int id) {
        return listRepository.findById(id);
    }
}
