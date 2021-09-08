package com.team.backend.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtsHolder {

    private String debtor;
    private String creditor;
    private BigDecimal balance;

    public DebtsHolder orderDebt() {
        if (this.getDebtor().compareTo(this.getCreditor()) >= 0) {
            String newDebtor = this.getCreditor();
            String newCreditor = this.getDebtor();

            this.setDebtor(newDebtor);
            this.setCreditor(newCreditor);
            this.setBalance(this.getBalance().multiply(BigDecimal.valueOf(-1)));
        }
        return this;
    }

    public Boolean filterDebts() {
        return this.getBalance().compareTo(BigDecimal.ZERO) != 0 && this.getDebtor().compareTo(this.getCreditor()) != 0;
    }

    public List<DebtsHolder> tidyDebts(List<DebtsHolder> debtsList) {
        List<DebtsHolder> tempList = new ArrayList<>();
        for (DebtsHolder debt : debtsList) {
            tempList.add(debt.orderDebt());
        }

//        return tempList.stream().sorted().collect(Collectors.toList());
        return tempList;
    }
}
