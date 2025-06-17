package com.welhire.persistence.repository.sql;

import com.welhire.persistence.entity.sql.JdCvMapping;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JdCvMappingRepository extends JpaRepository<JdCvMapping, String> {

    Optional<JdCvMapping> findByJdRefIdAndCvUploadRefId(String jdRefId, UUID cvUploadRefId);

    boolean existsByJdRefIdAndCvUploadRefId( String jdRefId, UUID id);
}
