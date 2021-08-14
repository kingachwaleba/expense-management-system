package com.team.backend.service;

import com.team.backend.model.List;
import com.team.backend.model.ListDetail;

public interface ListDetailService {

    void save(ListDetail listDetail, List shoppingList);
}
