package com.team.backend.helpers;

import com.team.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtsHolder {

    private User debtor;
    private User creditor;
    private BigDecimal howMuch;
}
