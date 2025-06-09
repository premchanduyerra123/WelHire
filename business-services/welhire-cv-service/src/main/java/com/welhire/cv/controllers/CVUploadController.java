package com.welhire.cv.controllers;


import com.welhire.cv.services.CVUploadService;
import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.shared.dto.v1.CVUploadResponse;
import com.welhire.shared.dto.v1.MultiCVUploadRequest;
import com.welhire.shared.dto.wrapper.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cv-uploads")
@RequiredArgsConstructor
public class CVUploadController {

    private final CVUploadService uploadService;


    @PostMapping(
            value = "/multi",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<List<CVUploadResponse>>> uploadMultiple(
            @RequestPart("meta") @Valid MultiCVUploadRequest meta,
            @RequestPart("files") MultipartFile[] files){

        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("At least one file is required."));
        }

        // 1) Validate content types
        List<String> allowed = List.of(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );

        List<String> invalidFiles = new ArrayList<>();
        for (MultipartFile f : files) {
            String ct = f.getContentType();
            if (ct == null || !allowed.contains(ct.toLowerCase())) {
                invalidFiles.add(f.getOriginalFilename());
            }
        }

        if (!invalidFiles.isEmpty()) {
            String msg = "Invalid file type for: " + String.join(", ", invalidFiles)
                    + ". Only PDF, DOC and DOCX are supported.";
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(ApiResponse.failure(msg));
        }


    // 2) If we get here, all files have valid types
        List<CVUploadResponse> uploadResults = uploadService.uploadFiles(meta, Arrays.asList(files));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Files processed", uploadResults));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<CandidateCVUpload>>> listAllUploads(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {

        // parse "sort" into field and direction, e.g. "createdAt,desc"
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = Sort.Direction.fromString(sortParams.length > 1
                ? sortParams[1]
                : "desc");

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CandidateCVUpload> uploadsPage = uploadService.getAllUploads(pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Paginated uploads fetched", uploadsPage)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CandidateCVUpload>> getById(
            @PathVariable("id") String id) {

        return uploadService.getById(id)
                .map(cv -> ResponseEntity.ok(ApiResponse.success("Found", cv)))
                .orElseGet(() -> ResponseEntity
                        .status(404)
                        .body(ApiResponse.failure("Not found id=" + id)));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CandidateCVUpload>>> search(
            @RequestParam Map<String,String> allParams,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {

        // pull out pagination params so they don't become filters
        allParams.remove("page");
        allParams.remove("size");
        allParams.remove("sort");

        // parse sort param
        String[] sortParts = sort.split(",");
        Sort.Direction dir = Sort.Direction.fromString(sortParts[1]);
        Sort   s   = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Page<CandidateCVUpload> results =
                uploadService.searchBy(allParams, pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Search results", results)
        );
    }

}
