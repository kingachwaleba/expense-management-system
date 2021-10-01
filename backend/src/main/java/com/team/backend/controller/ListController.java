package com.team.backend.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import com.team.backend.exception.ListNotFoundException;
import com.team.backend.exception.StatusNotFoundException;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.exception.WalletNotFoundException;
import com.team.backend.helpers.ListHolder;
import com.team.backend.model.*;
import com.team.backend.repository.StatusRepository;
import com.team.backend.service.ListService;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingList(#id)")
    public ResponseEntity<?> one(@PathVariable int id) {
        ShoppingList shoppingList = listService.findById(id).orElseThrow(ListNotFoundException::new);
        java.util.List<String> deletedUserList = walletService.findDeletedUserList(shoppingList.getWallet());
        Map<String, Object> map = new HashMap<>();
        map.put("shoppingList", shoppingList);
        map.put("deletedUserList", deletedUserList);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/shopping-lists")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> all(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        return new ResponseEntity<>(listService.findAllByWallet(wallet), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/create-shopping-list")
    @PreAuthorize("@authenticationService.isWalletMember(#id)")
    public ResponseEntity<?> createList(@PathVariable int id, @Valid @RequestBody ListHolder listHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(WalletNotFoundException::new);

        listService.save(listHolder, wallet);

        return new ResponseEntity<>(listHolder.getList(), HttpStatus.OK);
    }

    @PutMapping("/shopping-list/edit/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingList(#id)")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody TextNode name) {
        ShoppingList updatedShoppingList = listService.findById(id).orElseThrow(ListNotFoundException::new);

        updatedShoppingList.setName(name.asText());

        listService.save(updatedShoppingList);

        return new ResponseEntity<>(updatedShoppingList, HttpStatus.OK);
    }

    @PutMapping("/change-list-status/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingList(#id)")
    public ResponseEntity<?> changeStatus(@PathVariable int id, @RequestBody int statusId) {
        ShoppingList updatedList = listService.findById(id).orElseThrow(ListNotFoundException::new);
        Status chosenStatus = statusRepository.findById(statusId).orElseThrow(StatusNotFoundException::new);
        Status pendingStatus = statusRepository.findByName("oczekujący").orElseThrow(StatusNotFoundException::new);
        Status completedStatus = statusRepository.findByName("zrealizowany").orElseThrow(StatusNotFoundException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        updatedList.setStatus(chosenStatus);

        if (chosenStatus.equals(pendingStatus))
            updatedList.setUser(null);
        else
            updatedList.setUser(user);

        for (ListDetail listDetail : updatedList.getListDetailSet()) {
            Status listDetailStatus = listDetail.getStatus();

            if (!listDetailStatus.equals(completedStatus)) {
                listDetail.setStatus(chosenStatus);
                if (chosenStatus.equals(pendingStatus))
                    listDetail.setUser(null);
                else
                    listDetail.setUser(user);
            }
        }

        listService.save(updatedList);

        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }

    @DeleteMapping("/shopping-list/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingList(#id)")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ShoppingList shoppingList = listService.findById(id).orElseThrow(ListNotFoundException::new);

        listService.delete(shoppingList);

        return new ResponseEntity<>("The given list was deleted!", HttpStatus.OK);
    }
}
