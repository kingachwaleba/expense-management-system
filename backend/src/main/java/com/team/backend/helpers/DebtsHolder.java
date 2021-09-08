package com.team.backend.helpers;

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

    private String debtor;
    private String creditor;
    private BigDecimal balance;
    private int id;

    public void orderDebt() {
        if (this.getDebtor().compareTo(this.getCreditor()) >= 0)
            changeBalance();
    }

    public void changeBalance() {
        String newDebtor = this.getCreditor();
        String newCreditor = this.getDebtor();

        this.setDebtor(newDebtor);
        this.setCreditor(newCreditor);
        this.setBalance(this.getBalance().multiply(BigDecimal.valueOf(-1)));
    }
}
