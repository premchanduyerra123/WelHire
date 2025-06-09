package com.welhire.cv.services;

import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.persistence.entity.ParsedCandidateCV;
import com.welhire.persistence.repository.CandidateCVUploadRepository;
import com.welhire.persistence.repository.ParsedCandidateCVRepository;
import com.welhire.cv.client.ParsingClient;

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

    @Async
    public void parseAsync(CandidateCVUpload upload) {

        log.info("parseAsync running on thread: {}", Thread.currentThread().getName());

        // mark start
        upload.setParseStartTime(LocalDateTime.now());
        uploadRepo.save(upload);

        // only parse if missing
        if (parsedRepo.findByJdContentId(upload.getId()).isEmpty()) {
            Map<String,Object> parsedJson = parsingClient.parseCV(
                    new ParseRequest(upload.getJdContentId(), upload.getFilePath())
            );

            ParsedCandidateCV pcv = ParsedCandidateCV.fromMap(parsedJson);
            pcv.setJdContentId(upload.getJdContentId());
            pcv.setCvUploadId(upload.getId());
            parsedRepo.save(pcv);
        }

        // mark complete
        upload.setIsParsed(true);
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

