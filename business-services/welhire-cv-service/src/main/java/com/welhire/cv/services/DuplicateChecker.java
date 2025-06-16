package com.welhire.cv.services;

import com.welhire.persistence.entity.CandidateCVUpload;
import com.welhire.persistence.repository.CandidateCVUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DuplicateChecker {
    private final CandidateCVUploadRepository repo;

    public Optional<CandidateCVUpload> findDuplicate(String fileHash) {
        return repo.findByFileHash( fileHash);
    }
}