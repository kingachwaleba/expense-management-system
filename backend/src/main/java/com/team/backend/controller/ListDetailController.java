package com.team.backend.controller;

import com.team.backend.exception.ListDetailNotFoundException;
import com.team.backend.exception.ListNotFoundException;
import com.team.backend.exception.StatusNotFoundException;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.model.ListDetail;
import com.team.backend.model.ShoppingList;
import com.team.backend.model.Status;
import com.team.backend.model.User;
import com.team.backend.repository.StatusRepository;
import com.team.backend.service.ListDetailService;
import com.team.backend.service.ListService;
import com.team.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingList(#id)")
    public ResponseEntity<?> addOne(@PathVariable int id, @Valid @RequestBody ListDetail listDetail,
                                    BindingResult bindingResult) {
        if (listDetailService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(listDetailService.getErrorList(bindingResult), HttpStatus.BAD_REQUEST);
        ShoppingList shoppingList = listService.findById(id).orElseThrow(ListNotFoundException::new);

        listDetailService.save(listDetail, shoppingList);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

    @PutMapping("/change-element-status/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingListDetail(#id)")
    public ResponseEntity<?> changeStatus(@PathVariable int id, @RequestBody int statusId) {
        ListDetail updatedElement = listDetailService.findById(id).orElseThrow(ListDetailNotFoundException::new);
        Status chosenStatus = statusRepository.findById(statusId).orElseThrow(StatusNotFoundException::new);
        User user = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        updatedElement.setStatus(chosenStatus);

        if (statusId == 3)
            updatedElement.setUser(null);
        else
            updatedElement.setUser(user);

        listDetailService.save(updatedElement);

        return new ResponseEntity<>(updatedElement.getList(), HttpStatus.OK);
    }

    @PutMapping("/edit-list-element/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingListDetail(#id)")
    public ResponseEntity<?> editListElement(@PathVariable int id, @Valid @RequestBody ListDetail listDetail,
                                             BindingResult bindingResult) {
        if (listDetailService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(listDetailService.getErrorList(bindingResult), HttpStatus.BAD_REQUEST);

        ListDetail updatedElement = listDetailService.findById(id).orElseThrow(ListDetailNotFoundException::new);

        updatedElement.setName(listDetail.getName());
        updatedElement.setQuantity(listDetail.getQuantity());
        updatedElement.setUnit(listDetail.getUnit());

        listDetailService.save(updatedElement);

        return new ResponseEntity<>(updatedElement.getList(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-list-element/{id}")
    @PreAuthorize("@authenticationService.isWalletMemberByShoppingListDetail(#id)")
    public ResponseEntity<?> deleteOne(@PathVariable int id) {
        ListDetail listDetail = listDetailService.findById(id).orElseThrow(ListDetailNotFoundException::new);
        ShoppingList shoppingList = listDetail.getList();

        listDetailService.delete(listDetail);

        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }
}
