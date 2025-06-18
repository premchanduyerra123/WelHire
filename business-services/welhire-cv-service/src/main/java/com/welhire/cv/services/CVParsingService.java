package com.welhire.cv.services;

import com.welhire.cv.config.ResourceMultipartFile;
import com.welhire.exceptions.ParsedCvNotFoundException;
import com.welhire.persistence.entity.mongo.CvParsed;
import com.welhire.persistence.entity.sql.CvUpload;
import com.welhire.persistence.repository.mongo.CvParsedRepository;
import com.welhire.persistence.repository.sql.CvUploadRepository;
import com.welhire.cv.client.ParsingClient;
import com.welhire.shared.dto.enums.CandidateStatus;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CVParsingService {

    private final CvParsedRepository parsedRepo;
    private final CvUploadRepository uploadRepo;
    private final ParsingClient parsingClient;
    private final MongoTemplate mongoTemplate;
    private final FileSystemStorageService storageService;
    private final CandidateCreationService creationService;

    @Async("taskExecutor")
    public void parseAndCreateAsync(CvUpload upload,
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
            upload.setParseStartTime(Instant.now());
            uploadRepo.save(upload);

            try {
                MultipartFile mf = new ResourceMultipartFile( "file", res, MediaType.APPLICATION_OCTET_STREAM_VALUE );
                CvParsed pcv = parsingClient.parseCV(mf);
                pcv.setCvUploadRefId(upload.getId());
                pcv.setCreatedBy(meta.getEmail());
                parsedRepo.save(pcv);

                upload.setParseStatus(ParseStatus.CV_PARSED_SUCCESS);
                upload.setParseEndTime(Instant.now());
            } catch (Exception e) {
                log.error("[{}] parsing error", upload.getId(), e);
                fail(upload, ParseStatus.CV_PARSED_FAILURE, e.getMessage());
                return;
            }

            upload.setUpdatedBy(meta.getEmail());
            uploadRepo.save(upload);
        }

        // 2) CANDIDATE CREATION
        upload.setCandidateCreateStatus(CandidateStatus.CANDIDATE_CREATION_PROCESSING);
        upload.setCandidateCreateStartTime(Instant.now());
        uploadRepo.save(upload);

        try {
            CvParsed pcv = parsedRepo
                    .findByCvUploadRefId(upload.getId())
                    .orElseThrow(() -> new IllegalStateException("No parsed CV for " + upload.getId()));

            CandidateCreationResponse resp = creationService.createCandidate(pcv);
            upload.setCandidateId(resp.getCandidateId());
            upload.setCandidateCreateStatus(CandidateStatus.CANDIDATE_CREATION_SUCCESS);
            upload.setCandidateCreateEndTime(Instant.now());
        } catch (Exception e) {
            log.error("[{}] creation error", upload.getId(), e);
            upload.setCandidateCreateStatus(CandidateStatus.CANDIDATE_CREATION_FAILURE);
            upload.setCandidateCreateErrorMessage(e.getMessage());
        }

        upload.setUpdatedBy(meta.getEmail());
        uploadRepo.save(upload);

        log.info("[{}] pipeline complete (status={})",
                upload.getId(), upload.getParseStatus());
    }

    private void fail(CvUpload upload, ParseStatus status, String msg) {
        upload.setParseStatus(status);
        upload.setParseErrorMessage(msg);
        uploadRepo.save(upload);
    }

    public Optional<CvParsed> getById(String id) {
        return parsedRepo.findById(id);

    }

    public Page<CvParsed> searchBy(Map<String, String> filters,
                                            Pageable pageable) {
        Query q = new Query();
        filters.forEach((f, v) ->
                q.addCriteria(Criteria.where(f).regex(".*" + Pattern.quote(v.trim()) + ".*", "i"))
        );
        long total = mongoTemplate.count(q, CvParsed.class);
        List<CvParsed> results = mongoTemplate.find(q.with(pageable), CvParsed.class);
        return new PageImpl<>(results, pageable, total);
    }
}
