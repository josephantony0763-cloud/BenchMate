package com.antony.benchmate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(
            @Value("${file.upload-dir:uploads/notes}") String uploadDir) {

        this.uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory",
                    e
            );
        }
    }

    // =====================================================
    // STORE FILE
    // =====================================================

    public String storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new RuntimeException(
                    "Invalid file name"
            );
        }

        String extension = "";

        int dotIndex =
                originalFileName.lastIndexOf('.');

        if (dotIndex > 0) {
            extension =
                    originalFileName
                            .substring(dotIndex)
                            .toLowerCase();
        }

        if (!".pdf".equals(extension)) {
            throw new RuntimeException(
                    "Only PDF files are allowed"
            );
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path targetLocation =
                uploadPath
                        .resolve(fileName)
                        .normalize();

        if (!targetLocation.startsWith(uploadPath)) {
            throw new RuntimeException(
                    "Invalid file path"
            );
        }

        try {

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to store file",
                    e
            );
        }

        return fileName;
    }

    // =====================================================
    // LOAD FILE
    // =====================================================

    public Resource loadFileAsResource(
            String fileName) {

        try {

            Path filePath =
                    uploadPath
                            .resolve(fileName)
                            .normalize();

            if (!filePath.startsWith(uploadPath)) {
                throw new RuntimeException(
                        "Invalid file path"
                );
            }

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (resource.exists() &&
                    resource.isReadable()) {

                return resource;
            }

            throw new RuntimeException(
                    "File not found: " + fileName
            );

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Could not read file: " + fileName,
                    e
            );
        }
    }

    // =====================================================
    // DELETE FILE
    // =====================================================

    public void deleteFile(String fileName) {

        Path filePath =
                uploadPath
                        .resolve(fileName)
                        .normalize();

        if (!filePath.startsWith(uploadPath)) {
            throw new RuntimeException(
                    "Invalid file path"
            );
        }

        try {

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not delete file: " + fileName,
                    e
            );
        }
    }
}
