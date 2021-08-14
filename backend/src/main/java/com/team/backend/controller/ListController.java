package com.team.backend.controller;

import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.repository.StatusRepository;
import com.team.backend.service.ListService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        List shoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/create-shopping-list")
    public ResponseEntity<?> createWallet(@PathVariable int id, @Valid @RequestBody ListHolder listHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        listService.save(listHolder, wallet);

        return new ResponseEntity<>(listHolder.getList(), HttpStatus.OK);
    }

    @PutMapping("/shopping-list/edit/{id}")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody String name) {
        List updatedShoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        updatedShoppingList.setName(name);

        listService.save(updatedShoppingList);

        return new ResponseEntity<>(updatedShoppingList, HttpStatus.OK);
    }
}
