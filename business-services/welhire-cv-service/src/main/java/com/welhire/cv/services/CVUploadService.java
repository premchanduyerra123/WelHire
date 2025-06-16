package com.welhire.cv.services;

import com.welhire.persistence.entity.sql.CandidateCvUpload;
import com.welhire.persistence.entity.sql.JdCvMapping;
import com.welhire.persistence.repository.sql.CandidateCVUploadRepository;
import com.welhire.persistence.repository.sql.JdCvMappingRepository;
import com.welhire.shared.dto.enums.ParseStatus;
import com.welhire.shared.dto.utils.FileHashUtil;
import com.welhire.shared.dto.v1.CVUploadResponse;
import com.welhire.shared.dto.v1.MultiCVUploadRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


@Service
@RequiredArgsConstructor
public class CVUploadService {

    private final CandidateCVUploadRepository uploadRepo;
    private final FileSystemStorageService storageService;
    private final CVParsingService parsingService;
    private final MongoTemplate mongoTemplate;
    private final JdCvMappingRepository mappingRepo;
    private final DuplicateChecker duplicateChecker;

    public List<CVUploadResponse> uploadFiles(MultiCVUploadRequest meta,
                                              List<MultipartFile> files) {
        List<CVUploadResponse> responses = new ArrayList<>();
        String ts = DateTimeFormatter
                .ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .format(LocalDateTime.now());

        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            CVUploadResponse resp;
            try {
                String hash = FileHashUtil.calculateMD5(file);
                boolean isDuplicate = duplicateChecker.findDuplicate(hash)
                        .map(existing -> {
                            // map to existing entity
                            existing.setUpdatedAt(LocalDateTime.now());
                            uploadRepo.save(existing);
                            return true;
                        }).orElse(false);

                CandidateCvUpload upload;
                if (isDuplicate) {
                    upload = duplicateChecker.findDuplicate(hash).get();
                } else {
                    String path = storageService.storeFile(
                            file, meta.getJdRefId(), meta.getEmail(), ts
                    );
                    upload = CandidateCvUpload.builder()
                            .userEmail(meta.getEmail())
                            .jdRefId(meta.getJdRefId())
                            .cvName(name)
                            .filePath(path)
                            .fileHash(hash)
                            .createdAt(LocalDateTime.now())
                            .createdBy(meta.getEmail())
                            .parseStatus(ParseStatus.CV_UPLOADED)
                            .build();

                     upload = uploadRepo.save(upload);
                }


                JdCvMapping jdCvMapping=   JdCvMapping.builder()
                        .jdRefId(meta.getJdRefId())
                        .cvUploadRefId(upload.getId())
                        .createdAt(LocalDateTime.now())
                        .createdBy(meta.getEmail())
                        .build();

                // record mapping
                mappingRepo.save(jdCvMapping);

                // kick off async pipeline
                parsingService.parseAndCreateAsync(upload, meta, isDuplicate);

                resp = new CVUploadResponse(
                        upload.getId(),
                        name,
                        upload.getFilePath(),
                        isDuplicate ? ParseStatus.CV_PARSED_SUCCESS : ParseStatus.CV_UPLOADED,
                        isDuplicate ? "Duplicate: scheduled candidate-creation async" : "Uploaded and queued"
                );
            } catch (IOException ex) {
                resp = new CVUploadResponse(
                        null, name, null,
                        ParseStatus.CV_PARSED_FAILURE,
                        "Upload error: " + ex.getMessage());
            }
            responses.add(resp);
        }
        return responses;
    }



    public Optional<CandidateCvUpload> getById(String id) {
        return uploadRepo.findById(id);
    }


    public Page<CandidateCvUpload> searchBy(
            Map<String,String> filters,
            Pageable pageable) {

        Query query = new Query();

        // Build an ilike (regex, case-insensitive, ".*value.*") for each param:
        filters.forEach((field, value) -> {
            String escaped = Pattern.quote(value.trim());
            String regex = String.format(".*%s.*", escaped);
            query.addCriteria(Criteria.where(field)
                    .regex(regex, "i"));
        });

        long total = mongoTemplate.count(query, CandidateCvUpload.class);
        query.with(pageable);
        List<CandidateCvUpload> list = mongoTemplate.find(query, CandidateCvUpload.class);

        return new PageImpl<>(list, pageable, total);
    }


    public Page<CandidateCvUpload> getAllUploads(Pageable pageable) {
        return uploadRepo.findAll(pageable);
    }
}
