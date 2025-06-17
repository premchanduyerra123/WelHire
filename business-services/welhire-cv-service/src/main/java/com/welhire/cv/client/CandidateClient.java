package com.welhire.cv.client;


import java.util.Map;

import com.welhire.persistence.entity.mongo.CvParsed;
import com.welhire.shared.dto.v1.CandidateCreationResponse;
import com.welhire.shared.dto.v1.CreateCandidateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "candidate-service", url = "${candidate.service.url}")
 public interface CandidateClient {

    @PostMapping("/candidates")
    CandidateCreationResponse createCandidate(@RequestBody CvParsed req);

}

