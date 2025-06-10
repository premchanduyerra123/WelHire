package com.welhire.cv.services;

import com.welhire.cv.client.CandidateClient;
import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.persistence.entity.ParsedCandidateCV;
import com.welhire.persistence.repository.CandidateCVUploadRepository;
import com.welhire.persistence.repository.ParsedCandidateCVRepository;
import com.welhire.cv.client.ParsingClient;

import com.welhire.shared.dto.enums.ParseStatus;
import com.welhire.shared.dto.v1.CreateCandidateRequest;
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
    @Async("taskExecutor")
    public void parseAsync(CandidateCVUpload upload) {
        log.info("parseAsync on {} for uploadId={}", Thread.currentThread().getName(), upload.getId());

        upload.setParseStartTime(LocalDateTime.now());
        upload.setParseStatus(ParseStatus.PROCESSING);
        uploadRepo.save(upload);

        // 1) parse if needed
        //need to change the logic of file hashing
        ParsedCandidateCV pcv = parsedRepo.findByJdContentId(upload.getId())
                .orElseGet(() -> {
                    Map<String,Object> parsedJson = parsingClient.parseCV(
                            new ParseRequest(upload.getJdContentId(), upload.getFilePath()));

                    ParsedCandidateCV newly = ParsedCandidateCV.fromMap(parsedJson);
                    newly.setJdContentId(upload.getJdContentId());
                    newly.setCvUploadId(upload.getId());
                    return parsedRepo.save(newly);
                });

        // 2) now call candidate‐service if not yet done
        if (!Boolean.TRUE.equals(pcv.getCandidateCreated())) {
            log.info("Calling candidate‐service for parsedId={}", pcv.getId());
            var req = new CreateCandidateRequest(
                    pcv.getJdContentId(),
                    pcv.getId(),
                    pcv.getCvUploadId(),
                    "FIXED_JD_CV_PARSED_ID"
            );
            Map<String,Object> resp = candidateClient.createCandidate(req);
            log.info("Candidate‐service response: {}", resp);

            // update the flag + optional returned candidateId
            pcv.setCandidateCreated(true);
            if (resp.containsKey("candidateId")) {
                pcv.setCandidateId((String) resp.get("candidateId"));
            }
            parsedRepo.save(pcv);
        }

        upload.setParseStatus(ParseStatus.SUCCESS);
        upload.setParseEndTime(LocalDateTime.now());
        uploadRepo.save(upload);
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

