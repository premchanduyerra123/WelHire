package com.welhire.cv.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService {

    @Value("${cv.upload.base-path}")
    private String basePath;

    public String storeFile(MultipartFile file, String jdId, String email,String timestamp) throws IOException {

        String hostname =  InetAddress.getLocalHost().getHostName();
        String tenantId = "TENANT123"; // Replace with real tenant ID

        // 1. Build directory path: basePath/hostname/tenantId/jdId/email/timestamp
        Path dirPath = Paths.get(basePath,hostname,tenantId, jdId, email, timestamp);
        Files.createDirectories(dirPath);

        // 2. Create a  filename
         String filename = file.getOriginalFilename();
        Path targetPath = dirPath.resolve(filename);

        // 3. Save file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 4. Build and return RELATIVE path using forward‐slashes:
        String relativePath = String.join("/",hostname, tenantId,jdId,email,timestamp,filename);
        return relativePath;
     }
    public Resource loadAsResource(String relativePath) {
        Path full = Paths.get(basePath).resolve(relativePath);
        return new FileSystemResource(full.toFile());
    }
}
