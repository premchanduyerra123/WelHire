package com.welhire.cv.services;

import com.welhire.shared.dto.v1.CandidateProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateProfileService {
    CandidateProfileDTO createCandidate(CandidateProfileDTO dto);
    CandidateProfileDTO getCandidate(String externalId);
    Page<CandidateProfileDTO> getAllCandidates(Pageable pageable);
    CandidateProfileDTO updateCandidate(String externalId, CandidateProfileDTO dto);
    void deleteCandidate(String externalId);
}

