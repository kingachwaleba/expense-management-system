package com.team.backend.controller;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.service.ListService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ListController {

    private final WalletService walletService;
    private final ListService listService;

    public ListController(WalletService walletService, ListService listService) {
        this.walletService = walletService;
        this.listService = listService;
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
