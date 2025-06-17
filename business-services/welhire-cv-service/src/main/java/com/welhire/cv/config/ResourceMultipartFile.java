package com.welhire.cv.config;

import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ResourceMultipartFile implements MultipartFile {
    private final Resource resource;
    private final String name;
    private final String originalFilename;
    private final String contentType;

    public ResourceMultipartFile(String name,
                                 Resource resource,
                                 String contentType) {
        this.name = name;
        this.resource = resource;
        this.originalFilename = resource.getFilename();
        this.contentType = contentType;
    }

    @Override public String getName() { return name; }
    @Override public String getOriginalFilename() { return originalFilename; }
    @Override public String getContentType() { return contentType; }
    @Override public boolean isEmpty() {
        try {
            return resource.contentLength() == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public long getSize() {
        try {
            return resource.contentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override public byte[] getBytes() throws IOException { return StreamUtils.copyToByteArray(resource.getInputStream()); }
    @Override public InputStream getInputStream() throws IOException { return resource.getInputStream(); }
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (InputStream in = resource.getInputStream()) {
            Files.copy(in, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

