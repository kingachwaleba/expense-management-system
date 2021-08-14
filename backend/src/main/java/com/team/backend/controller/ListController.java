package com.team.backend.controller;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.service.ListService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ListController {

    private final WalletService walletService;
    private final ListService listService;

    public ListController(WalletService walletService, ListService listService) {
        this.walletService = walletService;
        this.listService = listService;
    }

    @GetMapping("/shopping-list/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        com.team.backend.model.List shoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(listService.showListDetails(shoppingList), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/create-shopping-list")
    public ResponseEntity<?> createWallet(@PathVariable int id, @Valid @RequestBody ListHolder listHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        listService.save(listHolder, wallet);

        Map<String, Object> map = new HashMap<>();

        map.put("walletId", wallet.getId());
        map.put("shoppingListId", listHolder.getList().getId());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
