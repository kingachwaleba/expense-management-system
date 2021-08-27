package com.team.backend.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.repository.StatusRepository;
import com.team.backend.service.ListService;
import com.team.backend.service.UserService;
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
    private final StatusRepository statusRepository;
    private final UserService userService;

    public ListController(WalletService walletService, ListService listService, StatusRepository statusRepository,
                          UserService userService) {
        this.walletService = walletService;
        this.listService = listService;
        this.statusRepository = statusRepository;
        this.userService = userService;
    }

    @GetMapping("/shopping-list/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        List shoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/shopping-lists")
    public ResponseEntity<?> all(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);
        java.util.List<List> shoppingList = listService.findAllByWallet(wallet);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/create-shopping-list")
    public ResponseEntity<?> createList(@PathVariable int id, @Valid @RequestBody ListHolder listHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        listService.save(listHolder, wallet);

        return new ResponseEntity<>(listHolder.getList(), HttpStatus.OK);
    }

    @PutMapping("/shopping-list/edit/{id}")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody TextNode name) {
        List updatedShoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        updatedShoppingList.setName(name.asText());

        listService.save(updatedShoppingList);

        return new ResponseEntity<>(updatedShoppingList, HttpStatus.OK);
    }

    @PutMapping("/change-list-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable int id, @RequestBody int statusId) {
        List updatedList = listService.findById(id).orElseThrow(RuntimeException::new);
        Status chosenStatus = statusRepository.findById(statusId).orElseThrow(RuntimeException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(RuntimeException::new);

        updatedList.setStatus(chosenStatus);

        if (statusId == 3)
            updatedList.setUser(null);
        else
            updatedList.setUser(user);

        for (ListDetail listDetail : updatedList.getListDetailSet()) {
            listDetail.setStatus(chosenStatus);

            if (statusId == 3)
                listDetail.setUser(null);
            else
                listDetail.setUser(user);
        }

        listService.save(updatedList);

        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }
}
