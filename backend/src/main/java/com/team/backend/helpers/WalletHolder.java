package com.team.backend.helpers;

import com.team.backend.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletHolder {

    @Valid
    private Wallet wallet;
    private List<String> userList;
}