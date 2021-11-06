package com.team.backend.controller;

import com.team.backend.model.Status;
import com.team.backend.repository.StatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {

    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @GetMapping("/statues")
    public ResponseEntity<?> getAllStatuses() {
        List<Status> statusList = statusRepository.findAll();

        return new ResponseEntity<>(statusList, HttpStatus.OK);
    }
}
