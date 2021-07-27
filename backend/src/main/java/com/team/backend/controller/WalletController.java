package com.team.backend.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.team.backend.helpers.WalletAssembler;
import com.team.backend.helpers.WalletHolder;
import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import com.team.backend.service.UserService;
import com.team.backend.service.WalletService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final WalletAssembler walletAssembler;

    public WalletController(WalletService walletService, UserService userService, WalletAssembler walletAssembler) {
        this.walletService = walletService;
        this.userService = userService;
        this.walletAssembler = walletAssembler;
    }

    @GetMapping("/wallet/{id}")
    public EntityModel<Wallet> one(@PathVariable int id) {
        Wallet wallet = walletService.findById(id)
                .orElseThrow(RuntimeException::new);

        return walletAssembler.toModel(wallet);
    }

    @GetMapping("/wallets")
    public CollectionModel<EntityModel<Wallet>> all() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        User user = userService.findByLogin(currentUserLogin).orElseThrow(RuntimeException::new);

        List<EntityModel<Wallet>> wallets = walletService.findByUserId(user.getId()).stream()
                .map(walletAssembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(wallets, linkTo(methodOn(WalletController.class).all()).withSelfRel());
    }

    @Transactional
    @PostMapping("/create-wallet")
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletHolder walletHolder) {
        walletService.save(walletHolder);

        EntityModel<Wallet> walletEntityModel = walletAssembler.toModel(walletHolder.getWallet());

        return ResponseEntity
                .created(walletEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(walletEntityModel);
    }

    @PutMapping("/wallet/{id}")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody Wallet newWallet) {
        Wallet updatedWallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        updatedWallet.setName(newWallet.getName());
        updatedWallet.setDescription(newWallet.getDescription());
        updatedWallet.setWalletCategory(newWallet.getWalletCategory());

        walletService.save(updatedWallet);

        EntityModel<Wallet> walletEntityModel = walletAssembler.toModel(updatedWallet);

        return ResponseEntity
                .created(walletEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(walletEntityModel);
    }
}
