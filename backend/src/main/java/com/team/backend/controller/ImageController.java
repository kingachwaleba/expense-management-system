package com.team.backend.controller;

import com.team.backend.service.ImageStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    private final ImageStorageService imageStorageService;

    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        if (!imageStorageService.ifProperType(multipartFile))
            return new ResponseEntity<>("It is not a proper type!", HttpStatus.EXPECTATION_FAILED);

        try {
            String newImageName = imageStorageService.save(multipartFile, "expenses");

            return new ResponseEntity<>(newImageName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFile(@RequestBody String imageName) {
        Resource file = imageStorageService.load(imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
