package com.welhire.cv.controllers;

 import com.welhire.cv.services.CVParsingService;
 import com.welhire.persistence.entity.mongo.CvParsed;
import com.welhire.shared.dto.wrapper.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
 import java.util.UUID;

@RestController
@RequestMapping("/api/cv-parsed")
@RequiredArgsConstructor
public class ParsedCVController {


    private final CVParsingService cVParsingService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CvParsed>> getById(
            @PathVariable("id") UUID id) {
        CvParsed pcv = cVParsingService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Found", pcv));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CvParsed>>> search(
            @RequestParam Map<String,String> allParams,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort
    ) {
        // remove pagination keys from filters
        allParams.remove("page");
        allParams.remove("size");
        allParams.remove("sort");

        var parts = sort.split(",");
        Sort.Direction dir = Sort.Direction.fromString(parts[1]);
        Sort s = Sort.by(dir, parts[0]);
        Pageable pg = PageRequest.of(page, size, s);

        Page<CvParsed> result = cVParsingService.searchBy(allParams, pg);
        return ResponseEntity.ok(ApiResponse.success("Search results", result));
    }
}
