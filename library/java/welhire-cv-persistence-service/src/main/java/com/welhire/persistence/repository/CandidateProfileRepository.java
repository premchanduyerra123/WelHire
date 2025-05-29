package com.welhire.persistence.repository;

import com.welhire.persistence.entity.CandidateProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByExternalId(String externalId);

    void deleteByExternalId(String externalId);

    Page<CandidateProfile> findAll(Pageable pageable);
}


