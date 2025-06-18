package com.welhire.persistence.repository.sql;

import com.welhire.persistence.entity.sql.CvUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CvUploadRepository extends JpaRepository<CvUpload, UUID> {
    Optional<CvUpload> findByFileHash( String fileHash);

    @Query("""
        select c
        from CvUpload c
        join JdCvMapping m on m.cvUploadRefId = c.id
        where m.jdRefId = :jdRefId
    """)
    Page<CvUpload> findAllByJd(String jdRefId, Pageable pageable);


 }

