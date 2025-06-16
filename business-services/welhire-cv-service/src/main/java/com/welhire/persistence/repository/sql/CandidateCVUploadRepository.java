package com.welhire.persistence.repository.sql;

import com.welhire.persistence.entity.sql.CandidateCvUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateCVUploadRepository extends JpaRepository<CandidateCvUpload, String> {
    Optional<CandidateCvUpload> findByFileHash( String fileHash);
 }

