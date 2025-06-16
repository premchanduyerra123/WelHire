package com.welhire.cv.services;

import com.welhire.cv.client.CandidateClient;
import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.persistence.entity.ParsedCandidateCV;
import com.welhire.persistence.repository.CandidateCVUploadRepository;
import com.welhire.persistence.repository.ParsedCandidateCVRepository;
import com.welhire.cv.client.ParsingClient;

import com.welhire.shared.dto.enums.ParseStatus;
import com.welhire.shared.dto.v1.CandidateCreationResponse;
import com.welhire.shared.dto.v1.CreateCandidateRequest;
import com.welhire.shared.dto.v1.MultiCVUploadRequest;
import com.welhire.shared.dto.v1.ParseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CVParsingService {

    private final ParsedCandidateCVRepository parsedRepo;
    private final CandidateCVUploadRepository uploadRepo;
    private final ParsingClient parsingClient;
    private final MongoTemplate mongoTemplate;
    private final CandidateClient candidateClient;   // ← inject
    private final CandidateCreationService creationService;


    @Async("taskExecutor")
    public void parseAndCreateAsync(CandidateCVUpload upload,
                                    MultiCVUploadRequest meta,
                                    boolean isParsed) {
        log.info("[{}] parseAndCreateAsync start; isParsed={}", upload.getId(), isParsed);

        // 1) PARSING PHASE (only if not already parsed)
        if (!isParsed) {
            upload.setParseStatus(ParseStatus.CV_PROCESSING);
            upload.setUpdatedAt(LocalDateTime.now());
            uploadRepo.save(upload);

            ParsedCandidateCV pcv;
            try {
                Map<String, Object> parsedJson = parsingClient.parseCV(
                        new ParseRequest(upload.getId(), upload.getFilePath())
                );
                pcv = ParsedCandidateCV.fromMap(parsedJson)
                        .toBuilder()
                        .jdContentId(upload.getJdRefId())
                        .cvUploadId(upload.getId())
                        .build();
                parsedRepo.save(pcv);

                upload.setParseStatus(ParseStatus.CV_PARSED_SUCCESS);
                upload.setParseEndTime(LocalDateTime.now());
            } catch (Exception ex) {
                log.error("[{}] parsing failed", upload.getId(), ex);
                upload.setParseStatus(ParseStatus.CV_PARSED_FAILURE);
                upload.setUpdatedAt(LocalDateTime.now());
                uploadRepo.save(upload);
                return;
            }

            upload.setUpdatedAt(LocalDateTime.now());
            uploadRepo.save(upload);
        }

        // 2) CANDIDATE CREATION PHASE
        upload.setParseStatus(ParseStatus.CANDIDATE_CREATION_PROCESSING);
        upload.setUpdatedAt(LocalDateTime.now());
        uploadRepo.save(upload);

        try {
            ParsedCandidateCV pcv = parsedRepo.findByCvUploadId(upload.getId())
                    .orElseThrow(() -> new RuntimeException("Parsed data missing for uploadId=" + upload.getId()));

            CandidateCreationResponse creationResp = creationService.createCandidate(pcv);
            upload.setCandidateId(creationResp.getCandidateId());
            upload.setParseStatus(ParseStatus.CANDIDATE_CREATION_SUCCESS);
        } catch (Exception ex) {
            log.error("[{}] candidate creation failed", upload.getId(), ex);
            upload.setParseStatus(ParseStatus.CANDIDATE_CREATION_FAILURE);
        }

        upload.setUpdatedAt(LocalDateTime.now());
        uploadRepo.save(upload);

        // 3) OPTIONAL JD-MATCHING PHASE
        // upload.setParseStatus(ParseStatus.CV_JD_MATCHING_PROCESSING);
        // upload.setUpdatedAt(LocalDateTime.now());
        // uploadRepo.save(upload);
        //
        // try {
        //     matchingService.match(upload.getId());
        //     upload.setParseStatus(ParseStatus.CV_JD_MATCHING_SUCCESS);
        // } catch (Exception ex) {
        //     log.error("[{}] JD matching failed", upload.getId(), ex);
        //     upload.setParseStatus(ParseStatus.CV_JD_MATCHING_FAILURE);
        // }
        // upload.setUpdatedAt(LocalDateTime.now());
        // uploadRepo.save(upload);

        log.info("[{}] parseAndCreateAsync complete; final status={}", upload.getId(), upload.getParseStatus());
    }

    public ParsedCandidateCV getById(String id) {
        return parsedRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parsed CV not found: " + id));
    }

    public Page<ParsedCandidateCV> searchBy(
            Map<String,String> filters,
            Pageable pageable
    ) {
        Query query = new Query();

        filters.forEach((field, value) -> {
            String regex = ".*" + Pattern.quote(value.trim()) + ".*";
            query.addCriteria(Criteria.where(field).regex(regex, "i"));
        });

        long total = mongoTemplate.count(query, ParsedCandidateCV.class);
        query.with(pageable);

        List<ParsedCandidateCV> list =
                mongoTemplate.find(query, ParsedCandidateCV.class);

        return new PageImpl<>(list, pageable, total);
    }
}

