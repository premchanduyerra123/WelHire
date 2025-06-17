package com.welhire.cv.client;

import java.util.Map;

import com.welhire.cv.config.MultipartSupportConfig;
import com.welhire.persistence.entity.mongo.ParsedCandidateCV;
import com.welhire.shared.dto.v1.ParseRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

//@FeignClient(name = "parsing-service", url = "${parsing.service.url}")
//public interface ParsingClient {
//    @PostMapping("/parse")
//    ParsedCandidateCV parseCV(@RequestBody ParseRequest req);
//}

@FeignClient(
        name          = "parsing-service",
        url           = "${parsing.service.url}",
        configuration = MultipartSupportConfig.class
)
public interface ParsingClient {
    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ParsedCandidateCV parseCV(@RequestPart("file") MultipartFile file);
}
