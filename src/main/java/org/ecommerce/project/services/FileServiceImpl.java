package org.ecommerce.project.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    public String uploadImage(String path, MultipartFile imageFile) throws IOException {
        String randomUUID = UUID.randomUUID().toString();
        String originalFileName = imageFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.lastIndexOf('.') == -1) {
            throw new IllegalArgumentException("Invalid file name or file without extension");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String generatedFileName = randomUUID.concat(extension);

        File destinationDirectory = new File(path);
        if (!destinationDirectory.exists()) {
            boolean created = destinationDirectory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create the directory: " + path);
            }
            System.out.println("Directory created at: " + destinationDirectory.getAbsolutePath());
        }

        Path destinationFilePath = Paths.get(path, generatedFileName);
        try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to save file: " + generatedFileName, e);
        }

        return generatedFileName;
    }
}
