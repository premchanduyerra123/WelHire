package com.welhire.cv.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService {

    @Value("${cv.upload.base-path}")
    private String basePath;

    /**
     * Saves the uploaded file under: <basePath>/<jdId>/<email>/<timestamp>/origFilename
     * Returns the absolute file path.
     */
    public String storeFile(MultipartFile file, String jdId, String email,String timestamp) throws IOException {


        // 1. Build directory path: basePath/jdId/email/timestamp
        Path dirPath = Paths.get(basePath, jdId, email, timestamp);
        Files.createDirectories(dirPath);

        // 2. Create a unique filename
        //String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String filename = file.getOriginalFilename();
        Path targetPath = dirPath.resolve(filename);

        // 3. Save file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 4. Build and return RELATIVE path using forward‐slashes:
        String relativePath = String.join("/",jdId,email,timestamp,filename);
        return relativePath;
       // return targetPath.toAbsolutePath().toString();
    }
}
