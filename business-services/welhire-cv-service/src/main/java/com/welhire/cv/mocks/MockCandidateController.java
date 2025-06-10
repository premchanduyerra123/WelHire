package com.welhire.cv.mocks;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/candidates")
public class MockCandidateController {

    public static class CreateCandidateRequest {
        public String jdContentId;
        public String cvParsedId;
        public String cvUploadId;
        public String jdCvParsedId;
    }

    @PostMapping
    public Map<String,Object> createCandidate(@RequestBody CreateCandidateRequest req) {
        // simulate some ID creation
        String newCandidateId = UUID.randomUUID().toString();
        return Map.of(
                "status",        "success",
                "candidateId",   newCandidateId,
                "jdContentId",   req.jdContentId,
                "cvParsedId",    req.cvParsedId,
                "cvUploadId",    req.cvUploadId,
                "jdCvParsedId",  req.jdCvParsedId
        );
    }
}

