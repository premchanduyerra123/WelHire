package com.welhire.cv.services;

import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.persistence.repository.CandidateCVUploadRepository;
import com.welhire.persistence.repository.ParsedCandidateCVRepository;
import com.welhire.shared.dto.v1.CVUploadResponse;
import com.welhire.shared.dto.v1.MultiCVUploadRequest;
import com.welhire.cv.client.ParsingClient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
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

    private final CandidateCVUploadRepository repository;
    private final ParsedCandidateCVRepository parsedRepo;
    private final FileSystemStorageService storageService;
    private final ParsingClient parsingClient;
    private final CVParsingService parsingService;
    private final MongoTemplate mongoTemplate;

    public List<CVUploadResponse> uploadFiles(MultiCVUploadRequest meta, List<MultipartFile> files) {
        List<CVUploadResponse> responses = new ArrayList<>();

        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(java.time.ZoneOffset.UTC)
                .format(Instant.now());

        for (MultipartFile file : files) {
            String originalName = file.getOriginalFilename();
            CVUploadResponse resp;
            try {
                // 1) Save file to disk
                String savedPath = storageService.storeFile(file, meta.getJdContentId(), meta.getEmail(), timestamp);

                // 2) Persist upload record and grab the generated ID
                CandidateCVUpload upload = new CandidateCVUpload();
                upload.setJdContentId(meta.getJdContentId());
                upload.setUserEmail(meta.getEmail());
                upload.setCvName(originalName);
                upload.setFilePath(savedPath);
                // parse flags default to false, times null
                upload = repository.save(upload);
                String cvParsedId = upload.getId();

                // 3) fire off parsing (async)
                parsingService.parseAsync(upload);

                // 4) return the jdContentId so client can track status
                resp = new CVUploadResponse(
                        cvParsedId,
                        originalName,
                        savedPath,
                        upload.getIsParsed(),
                        "Uploaded successfully"
                );
            } catch (IOException e) {
                resp = new CVUploadResponse(
                        null,
                        originalName,
                        null,
                        false,
                        "Failed to upload: " + e.getMessage()
                );
            }
            responses.add(resp);
        }

        return responses;
    }


    public Optional<CandidateCVUpload> getById(String id) {
        return repository.findById(id);
    }


    public Page<CandidateCVUpload> searchBy(
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

        long total = mongoTemplate.count(query, CandidateCVUpload.class);
        query.with(pageable);
        List<CandidateCVUpload> list = mongoTemplate.find(query, CandidateCVUpload.class);

        return new PageImpl<>(list, pageable, total);
    }


    public Page<CandidateCVUpload> getAllUploads(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
