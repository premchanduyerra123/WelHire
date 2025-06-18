package com.welhire.cv.controllers;

import com.welhire.cv.services.CVUploadService;
import com.welhire.persistence.entity.sql.CvUpload;
import com.welhire.shared.dto.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jds/{jdId}/cv-uploads")   // <-- nested route
@RequiredArgsConstructor
public class JdCvUploadController {

    private final CVUploadService uploadService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CvUpload>>> listByJd(
            @PathVariable String jdId,
            @RequestParam(value = "page", defaultValue = "0")  int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {

        String[] sp = sort.split(",");
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.fromString(sp[1]), sp[0]));

        Page<CvUpload> pageResult = uploadService.listUploadsForJd(jdId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Uploads for JD.", pageResult));
    }
}

