package com.welhire.cv.services;

import com.welhire.cv.config.ResourceMultipartFile;
import com.welhire.persistence.entity.mongo.ParsedCandidateCV;
import com.welhire.persistence.entity.sql.CandidateCvUpload;
import com.welhire.persistence.repository.mongo.ParsedCandidateCvRepository;
import com.welhire.persistence.repository.sql.CandidateCVUploadRepository;
import com.welhire.cv.client.ParsingClient;
import com.welhire.shared.dto.enums.ParseStatus;
import com.welhire.shared.dto.v1.CandidateCreationResponse;
import com.welhire.shared.dto.v1.MultiCVUploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CVParsingService {

    private final ParsedCandidateCvRepository parsedRepo;
    private final CandidateCVUploadRepository uploadRepo;
    private final ParsingClient parsingClient;
    private final MongoTemplate mongoTemplate;
    private final FileSystemStorageService storageService;
    private final CandidateCreationService creationService;

    @Async("taskExecutor")
    public void parseAndCreateAsync(CandidateCvUpload upload,
                                    MultiCVUploadRequest meta,
                                    boolean isParsed) {
        log.info("[{}] pipeline start (alreadyParsed={})", upload.getId(), isParsed);

        // 1) PARSING
        if (!isParsed) {
            Resource res = storageService.loadAsResource(upload.getFilePath());
            if (!res.exists() || !res.isReadable()) {
                fail(upload, ParseStatus.CV_PARSED_FAILURE, "File missing or unreadable");
                return;
            }

            upload.setParseStatus(ParseStatus.CV_PROCESSING);
            upload.setUpdatedAt(LocalDateTime.now());
            uploadRepo.save(upload);

            try {
                MultipartFile mf = new ResourceMultipartFile(
                        "file", res, MediaType.APPLICATION_OCTET_STREAM_VALUE
                );
                ParsedCandidateCV pcv = parsingClient.parseCV(mf);
                pcv.setCvUploadRefId(upload.getId());
                pcv.setCreatedBy(meta.getEmail());
                pcv.setCreatedAt(LocalDateTime.now());
                parsedRepo.save(pcv);

                upload.setParseStatus(ParseStatus.CV_PARSED_SUCCESS);
            } catch (Exception e) {
                log.error("[{}] parsing error", upload.getId(), e);
                fail(upload, ParseStatus.CV_PARSED_FAILURE, e.getMessage());
                return;
            }

            upload.setUpdatedAt(LocalDateTime.now());
            uploadRepo.save(upload);
        }

        // 2) CANDIDATE CREATION
        upload.setParseStatus(ParseStatus.CANDIDATE_CREATION_PROCESSING);
        upload.setUpdatedAt(LocalDateTime.now());
        uploadRepo.save(upload);

        try {
            ParsedCandidateCV pcv = parsedRepo
                    .findByCvUploadRefId(upload.getId())
                    .orElseThrow(() -> new IllegalStateException("No parsed CV for " + upload.getId()));

            CandidateCreationResponse resp = creationService.createCandidate(pcv);
            upload.setCandidateId(resp.getCandidateId());
            upload.setParseStatus(ParseStatus.CANDIDATE_CREATION_SUCCESS);
        } catch (Exception e) {
            log.error("[{}] creation error", upload.getId(), e);
            upload.setParseStatus(ParseStatus.CANDIDATE_CREATION_FAILURE);
        }

        upload.setUpdatedAt(LocalDateTime.now());
        uploadRepo.save(upload);

        log.info("[{}] pipeline complete (status={})",
                upload.getId(), upload.getParseStatus());
    }

    private void fail(CandidateCvUpload upload, ParseStatus status, String msg) {
        upload.setParseStatus(status);
        upload.setParseErrorMessage(msg);
        upload.setUpdatedAt(LocalDateTime.now());
        uploadRepo.save(upload);
    }

    public ParsedCandidateCV getById(String id) {
        return parsedRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parsed CV not found: " + id));
    }

    public Page<ParsedCandidateCV> searchBy(Map<String, String> filters,
                                            Pageable pageable) {
        Query q = new Query();
        filters.forEach((f, v) ->
                q.addCriteria(Criteria.where(f).regex(".*" + Pattern.quote(v.trim()) + ".*", "i"))
        );
        long total = mongoTemplate.count(q, ParsedCandidateCV.class);
        List<ParsedCandidateCV> results = mongoTemplate.find(q.with(pageable), ParsedCandidateCV.class);
        return new PageImpl<>(results, pageable, total);
    }
}
