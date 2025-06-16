package com.welhire.cv.client;

import java.util.Map;

import com.welhire.persistence.entity.mongo.ParsedCandidateCV;
import com.welhire.shared.dto.v1.ParseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "parsing-service", url = "${parsing.service.url}")
public interface ParsingClient {
    @PostMapping("/parse")
    ParsedCandidateCV parseCV(@RequestBody ParseRequest req);
}

