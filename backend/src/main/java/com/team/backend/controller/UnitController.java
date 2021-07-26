package com.team.backend.controller;

import com.team.backend.model.Unit;
import com.team.backend.repository.UnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnitController {

    private final UnitRepository unitRepository;

    public UnitController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping("/units")
    public ResponseEntity<?> getAllUnits() {
        List<Unit> unitList = unitRepository.findAll();

        return new ResponseEntity<>(unitList, HttpStatus.OK);
    }
}
