package com.welhire.cv.client;

import com.welhire.cv.config.MultipartSupportConfig;
import com.welhire.persistence.entity.mongo.CvParsed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(
        name          = "parsing-service",
        url           = "${parsing.service.url}",
        configuration = MultipartSupportConfig.class
)
public interface ParsingClient {
    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CvParsed parseCV(@RequestPart("file") MultipartFile file);
}
