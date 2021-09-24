package com.team.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    @Override
    public String save(MultipartFile multipartFile, String directory) {
        try {
            final Path root = Paths.get("uploads/" + directory);

            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
            String newImageName = date + "-" + Objects.requireNonNull(multipartFile.getOriginalFilename());

            Files.copy(multipartFile.getInputStream(), root.resolve(newImageName));

            return newImageName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String imageName, String directory) {
        try {
            final Path root = Paths.get("uploads/" + directory);
            Path file = root.resolve(imageName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the image!");
            }
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Error: " + exception.getMessage());
        }
    }

    @Override
    public boolean ifProperType(MultipartFile multipartFile) {
        List<String> contentTypes = Arrays.asList("image/jpg", "image/jpeg", "image/png");

        return contentTypes.contains(multipartFile.getContentType());
    }
}
