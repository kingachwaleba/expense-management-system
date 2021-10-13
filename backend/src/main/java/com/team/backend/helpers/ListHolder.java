package com.team.backend.helpers;

import com.team.backend.model.ListDetail;
import com.team.backend.model.ShoppingList;
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
public class ListHolder {

    @Valid
    private ShoppingList list;
    @Valid
    private List<ListDetail> listDetailList;
}
