package com.welhire.cv.controllers;

import com.welhire.cv.services.CandidateProfileService;
import com.welhire.shared.dto.v1.CandidateProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/candidates")
public class CVController {

    @Autowired
    private CandidateProfileService service;

    @PostMapping
    public ResponseEntity<CandidateProfileDTO> create(@RequestBody CandidateProfileDTO dto) {
        return new ResponseEntity<>(service.createCandidate(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<CandidateProfileDTO> get(@PathVariable("externalId") String externalId) {
        return ResponseEntity.ok(service.getCandidate(externalId));
    }

    @GetMapping
    public ResponseEntity<Page<CandidateProfileDTO>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllCandidates(PageRequest.of(page, size)));
    }


    @PutMapping("/{externalId}")
    public ResponseEntity<CandidateProfileDTO> update(@PathVariable String externalId,
                                                      @RequestBody CandidateProfileDTO dto) {
        return ResponseEntity.ok(service.updateCandidate(externalId, dto));
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> delete(@PathVariable String externalId) {
        service.deleteCandidate(externalId);
        return ResponseEntity.noContent().build();
    }
}


