package com.welhire.persistence.repository.mongo;

import com.welhire.persistence.entity.mongo.ParsedCandidateCV;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ParsedCandidateCvRepository  extends MongoRepository<ParsedCandidateCV, String> {

    Optional<ParsedCandidateCV> findByCvUploadRefId(String id);
}
