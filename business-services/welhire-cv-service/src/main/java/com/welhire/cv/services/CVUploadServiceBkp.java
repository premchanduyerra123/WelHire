//package com.welhire.cv.services;
//
//
//import com.welhire.persistence.entity.CandidateCVUpload;
//import com.welhire.persistence.repository.CandidateCVUploadRepository;
//import com.welhire.shared.dto.v1.CVUploadResponse;
//import com.welhire.shared.dto.v1.MultiCVUploadRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CVUploadServiceBkp {
//
//    @Autowired
//    private CandidateCVUploadRepository repository;
//
//    @Autowired
//    private FileSystemStorageService storageService;
//
//
//    public List<CVUploadResponse> uploadFiles(MultiCVUploadRequest meta, List<MultipartFile> files) {
//        List<CVUploadResponse> responses = new ArrayList<>();
//
//        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
//                .withZone(java.time.ZoneOffset.UTC)
//                .format(Instant.now());
//
//        for (MultipartFile file : files) {
//            String originalName = file.getOriginalFilename();
//            CVUploadResponse resp;
//            try {
//
//
//                // 1. Save file to local filesystem
//                String savedPath = storageService.storeFile(file, meta.getJdId(), meta.getEmail(),timestamp);
//
//                // 2. Persist metadata in DB
//                CandidateCVUpload entity = new CandidateCVUpload();
//                entity.setJdId(meta.getJdId());
//                entity.setUserEmail(meta.getEmail());
//                entity.setCvName(originalName);
//                entity.setFilePath(savedPath);
//                repository.save(entity);
//
//                // 3. Build success response for this file
//                resp = new CVUploadResponse(originalName, savedPath, true, "Uploaded successfully");
//            } catch (IOException e) {
//                // Build failure response for this file
//                resp = new CVUploadResponse(originalName, null, false, "Failed to upload: " + e.getMessage());
//            }
//            responses.add(resp);
//        }
//        return responses;
//    }
//
//
//    public Page<CandidateCVUpload> getAllUploads(Pageable pageable) {
//        return repository.findAll(pageable);
//    }
//
//}
