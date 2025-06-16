package com.welhire.persistence.repository;

import com.welhire.persistence.entity.CandidateCVUpload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateCVUploadRepository extends MongoRepository<CandidateCVUpload, String> {
    Optional<CandidateCVUpload> findByFileHash( String fileHash);
 }

