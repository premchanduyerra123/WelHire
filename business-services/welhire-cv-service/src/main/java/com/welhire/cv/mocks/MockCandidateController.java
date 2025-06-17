package com.welhire.cv.mocks;

import com.welhire.persistence.entity.mongo.CvParsed;
import com.welhire.shared.dto.v1.CandidateCreationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/candidates")
public class MockCandidateController {



    @PostMapping
    public ResponseEntity<CandidateCreationResponse> create(@RequestBody CvParsed req) {

        log.info("Received candidate creation request for uploadId={}", req.toString());

        log.info("Simulating delay of 10 seconds...");
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Sleep interrupted", e);
        }

        String candId = "CAND-" + System.currentTimeMillis();

        CandidateCreationResponse resp = new CandidateCreationResponse(
                candId,
                "SUCCESS",
                "Candidate created successfully for uploadId=" + req.getId()
        );
        return ResponseEntity.ok(resp);
    }
}

