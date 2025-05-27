package com.welhire.cv.controllers;

import com.welhire.persistence.entity.CandidateProfile;
import com.welhire.persistence.repository.CandidateProfileRepository;
 import com.welhire.shared.dto.wrapper.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv")

public class CVController {
    private final CandidateProfileRepository repo;

    public CVController(CandidateProfileRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.welhire.shared.dto.wrapper.ApiResponse<CandidateProfile>> getById(@PathVariable("id") Integer id) {
        return repo.findById(id)
                .map(data -> ResponseEntity.ok(new ApiResponse<CandidateProfile>("Found", data, true)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<CandidateProfile>("Not Found", null, false)));
    }
}

