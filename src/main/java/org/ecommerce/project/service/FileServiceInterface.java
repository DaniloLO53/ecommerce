package org.ecommerce.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileServiceInterface {
    String uploadImage(String path, MultipartFile imageFile) throws IOException;
}
