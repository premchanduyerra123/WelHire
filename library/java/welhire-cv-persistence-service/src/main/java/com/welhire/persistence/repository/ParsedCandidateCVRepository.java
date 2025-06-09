package com.welhire.persistence.repository;

import com.welhire.persistence.entity.ParsedCandidateCV;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ParsedCandidateCVRepository  extends MongoRepository<ParsedCandidateCV, String> {
    Optional<ParsedCandidateCV> findByJdContentId(String jdContentId);
}
