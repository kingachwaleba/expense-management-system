package com.team.backend.helpers;

import com.team.backend.model.Expense;
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
public class ExpenseHolder {

    @Valid
    private Expense expense;
    private List<String> userList;
}
