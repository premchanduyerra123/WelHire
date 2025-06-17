package com.welhire.cv.services;

import com.welhire.persistence.entity.sql.CandidateCvUpload;
import com.welhire.persistence.repository.sql.CandidateCVUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HashDuplicateChecker {
    private final CandidateCVUploadRepository repo;

    public Optional<CandidateCvUpload> findDuplicate(String fileHash) {
        return repo.findByFileHash( fileHash);
    }
}