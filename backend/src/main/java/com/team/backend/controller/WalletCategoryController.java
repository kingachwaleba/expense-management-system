package com.team.backend.controller;

import com.team.backend.model.WalletCategory;
import com.team.backend.repository.WalletCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WalletCategoryController {

    private final WalletCategoryRepository walletCategoryRepository;

    public WalletCategoryController(WalletCategoryRepository walletCategoryRepository) {
        this.walletCategoryRepository = walletCategoryRepository;
    }

    @GetMapping("/wallet-categories")
    public ResponseEntity<?> getAllWalletCategories() {
        List<WalletCategory> walletCategoryList = walletCategoryRepository.findAll();

        return new ResponseEntity<>(walletCategoryList, HttpStatus.OK);
    }
}
