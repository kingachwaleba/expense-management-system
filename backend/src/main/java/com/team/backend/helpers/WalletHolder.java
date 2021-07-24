package com.team.backend.helpers;

import com.team.backend.model.User;
import com.team.backend.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletHolder {

    private Wallet wallet;
    private List<User> userList;
}