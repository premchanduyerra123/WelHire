package com.welhire.cv.services;

import com.welhire.persistence.entity.sql.CvUpload;
import com.welhire.persistence.repository.sql.CvUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HashDuplicateChecker {
    private final CvUploadRepository repo;

    public Optional<CvUpload> findDuplicate(String fileHash) {
        return repo.findByFileHash( fileHash);
    }
}