package com.team.backend.controller;

import com.team.backend.model.List;
import com.team.backend.model.ListDetail;
import com.team.backend.service.ListDetailService;
import com.team.backend.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ListDetailController {

    private final ListService listService;
    private final ListDetailService listDetailService;

    public ListDetailController(ListService listService, ListDetailService listDetailService) {
        this.listService = listService;
        this.listDetailService = listDetailService;
    }

    @PostMapping("/shopping-list/{id}")
    public ResponseEntity<?> addOne(@PathVariable int id, @Valid @RequestBody ListDetail listDetail) {
        List shoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        listDetailService.save(listDetail, shoppingList);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

}
