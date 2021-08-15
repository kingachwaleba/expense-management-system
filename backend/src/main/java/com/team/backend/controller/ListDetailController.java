package com.team.backend.controller;

import com.team.backend.model.List;
import com.team.backend.model.ListDetail;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import com.team.backend.repository.StatusRepository;
import com.team.backend.service.ListDetailService;
import com.team.backend.service.ListService;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ListDetailController {

    private final ListService listService;
    private final ListDetailService listDetailService;
    private final StatusRepository statusRepository;
    private final UserService userService;

    public ListDetailController(ListService listService, ListDetailService listDetailService, StatusRepository statusRepository, UserService userService) {
        this.listService = listService;
        this.listDetailService = listDetailService;
        this.statusRepository = statusRepository;
        this.userService = userService;
    }

    @PostMapping("/shopping-list/{id}")
    public ResponseEntity<?> addOne(@PathVariable int id, @Valid @RequestBody ListDetail listDetail) {
        List shoppingList = listService.findById(id).orElseThrow(RuntimeException::new);

        listDetailService.save(listDetail, shoppingList);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

    @PutMapping("/change-element-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable int id, @RequestBody int statusId) {
        ListDetail updatedElement = listDetailService.findById(id).orElseThrow(RuntimeException::new);
        Status chosenStatus = statusRepository.findById(statusId).orElseThrow(RuntimeException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        User user = userService.findByLogin(currentUserLogin).orElseThrow(RuntimeException::new);

        updatedElement.setStatus(chosenStatus);

        if (statusId == 3)
            updatedElement.setUser(null);
        else
            updatedElement.setUser(user);

        listDetailService.save(updatedElement);

        return new ResponseEntity<>(updatedElement.getList(), HttpStatus.OK);
    }
}
