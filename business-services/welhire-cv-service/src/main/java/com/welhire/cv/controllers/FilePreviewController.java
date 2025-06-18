package com.welhire.cv.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
public class FilePreviewController {


    @Value("${cv.upload.base-path}")
    private String basePath;


    @GetMapping("/preview")
    public ResponseEntity<Resource> previewFile(@RequestParam("filePath") String relativePath) {
        try {
            // 1. Convert backslashes to platform file separator
            String normalized = relativePath.replace("\\", File.separator);

            // 2. Resolve against basePath
            Path fileOnDisk = Paths.get(basePath).resolve(normalized).normalize();

            // 3. Wrap in UrlResource
            Resource resource = new UrlResource(fileOnDisk.toUri());

            // 4. Verify existence/readability
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 5. Dynamically determine MIME type
            String contentType = Files.probeContentType(fileOnDisk);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    // “inline” lets browser preview if supported
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

