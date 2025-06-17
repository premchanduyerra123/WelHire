package com.welhire.persistence.repository.mongo;

import com.welhire.persistence.entity.mongo.CvParsed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface CvParsedRepository  extends MongoRepository<CvParsed, UUID> {

    Optional<CvParsed> findByCvUploadRefId(UUID id);
}
