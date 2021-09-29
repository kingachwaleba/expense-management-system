package com.team.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String save(MultipartFile multipartFile, String directory);
    Resource load(String imageName);
    boolean ifProperType(MultipartFile multipartFile);
}
