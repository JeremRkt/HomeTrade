package com.isep.hometrade.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUpload {

    public static void saveFile(String path, MultipartFile multipartFile) throws IOException {
        Path tempPath = Paths.get(path);
        if (!Files.exists(tempPath)) {
            Files.createDirectories(tempPath);
        }
        Path filePath = tempPath.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        Files.delete(filePath);
    }

}
