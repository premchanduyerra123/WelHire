package com.welhire.cv.services;

import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.persistence.repository.CandidateCVUploadRepository;
import com.welhire.persistence.repository.ParsedCandidateCVRepository;
import com.welhire.shared.dto.enums.ParseStatus;
import com.welhire.shared.dto.utils.FileHashUtil;
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

    private final CandidateCVUploadRepository repository;
    private final ParsedCandidateCVRepository parsedRepo;
    private final FileSystemStorageService storageService;
    private final ParsingClient parsingClient;
    private final CVParsingService parsingService;
    private final MongoTemplate mongoTemplate;

    private final CandidateCVUploadRepository uploadRepo;
    private final DuplicateChecker duplicateChecker;
    private final CandidateCreationService creationService;

    public List<CVUploadResponse> uploadFiles(MultiCVUploadRequest meta,
                                              List<MultipartFile> files) {
        List<CVUploadResponse> responses = new ArrayList<>();
        String ts = DateTimeFormatter
                .ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(java.time.ZoneOffset.UTC)
                .format(Instant.now());

        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            CVUploadResponse resp;
            try {
                String hash = FileHashUtil.calculateMD5(file);
                var dupOpt = duplicateChecker.findDuplicate(hash);

                if (dupOpt.isPresent()) {
                    CandidateCVUpload existing = dupOpt.get();

                    // kick off only candidate‐creation (parsing already done)
                    parsingService.parseAndCreateAsync(existing, meta, true);

                    resp = new CVUploadResponse(
                            existing.getId(),
                            name,
                            existing.getFilePath(),
                            ParseStatus.CV_PARSED_SUCCESS,
                            "Duplicate: scheduled candidate‐creation async"
                    );

                } else {
                    String saved = storageService.storeFile(
                            file, meta.getJdRefId(), meta.getEmail(), ts);
                    CandidateCVUpload upload = CandidateCVUpload.builder()
                            .userEmail(meta.getEmail())
                            .jdRefId(meta.getJdRefId())
                            .cvName(name)
                            .filePath(saved)
                            .fileHash(hash)
                            .parseStatus(ParseStatus.CV_UPLOADED)
                            .build();

                    upload.setCreatedAt(LocalDateTime.now());
                    upload.setCreatedBy(meta.getEmail());



                    upload = uploadRepo.save(upload);

                    parsingService.parseAndCreateAsync(upload, meta,false);

                    resp = new CVUploadResponse(upload.getId(), name,
                            saved, ParseStatus.CV_UPLOADED,
                            "Uploaded and queued");
                }
            } catch (IOException ex) {
                resp = new CVUploadResponse(null, name, null,
                        ParseStatus.CV_PARSED_FAILURE,
                        "Upload error: " + ex.getMessage());
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
