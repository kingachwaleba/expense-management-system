package com.team.backend.helpers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.team.backend.controller.WalletController;
import com.team.backend.model.Wallet;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class WalletAssembler implements RepresentationModelAssembler<Wallet, EntityModel<Wallet>> {

    @Override
    public EntityModel<Wallet> toModel(Wallet wallet) {
        return EntityModel.of(wallet,
                linkTo(methodOn(WalletController.class).one(wallet.getId())).withSelfRel(),
                linkTo(methodOn(WalletController.class).all()).withRel("wallets"));
    }
}
