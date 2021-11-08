package com.team.backend.controller;

import com.team.backend.config.ErrorMessage;
import com.team.backend.exception.ExpenseNotFoundException;
import com.team.backend.exception.UserNotFoundException;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.ImageStorageService;
import com.team.backend.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    private final ImageStorageService imageStorageService;
    private final ErrorMessage errorMessage;
    private final ExpenseService expenseService;
    private final UserService userService;

    public ImageController(ImageStorageService imageStorageService, ErrorMessage errorMessage,
                           ExpenseService expenseService, UserService userService) {
        this.imageStorageService = imageStorageService;
        this.errorMessage = errorMessage;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        if (!imageStorageService.ifProperType(multipartFile))
            return new ResponseEntity<>(errorMessage.get("image.type"), HttpStatus.EXPECTATION_FAILED);

        try {
            String newImageName = imageStorageService.save(multipartFile, "expenses");

            return new ResponseEntity<>(newImageName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFile(@RequestParam("imageName") String imageName) {
        Resource file = imageStorageService.load(imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/expense-files/{id}")
    public ResponseEntity<?> getExpenseFile(@PathVariable int id) {
        String imageName = expenseService.findById(id).orElseThrow(ExpenseNotFoundException::new).getReceipt_image();
        Resource file = imageStorageService.load(imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/user-files/{login}")
    public ResponseEntity<?> getUserFile(@PathVariable String login) {
        String imageName = userService.findByLogin(login).orElseThrow(UserNotFoundException::new).getImage();
        if (imageName == null)
            return new ResponseEntity<>("The user does not have an image!", HttpStatus.NOT_FOUND);
        
        Resource file = imageStorageService.load(imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
