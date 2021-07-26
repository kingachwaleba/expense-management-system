package com.team.backend.controller;

import com.team.backend.model.Category;
import com.team.backend.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();

        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }
}
