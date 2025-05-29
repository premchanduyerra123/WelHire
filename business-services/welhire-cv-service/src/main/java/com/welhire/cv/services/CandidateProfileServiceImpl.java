package com.welhire.cv.services;

import com.welhire.persistence.entity.CandidateProfile;
import com.welhire.persistence.repository.CandidateProfileRepository;
import com.welhire.shared.dto.v1.CandidateProfileDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CandidateProfileServiceImpl implements CandidateProfileService {

    @Autowired
    private CandidateProfileRepository repo;

    @Override
    public CandidateProfileDTO createCandidate(CandidateProfileDTO dto) {
        CandidateProfile entity = new ModelMapper().map(dto, CandidateProfile.class);
        entity = repo.save(entity);
        return new ModelMapper().map(entity, CandidateProfileDTO.class);
    }

    @Override
    public CandidateProfileDTO getCandidate(String externalId) {
        return repo.findByExternalId(externalId)
                .map(entity -> new ModelMapper().map(entity, CandidateProfileDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));
    }

    @Override
    public Page<CandidateProfileDTO> getAllCandidates(Pageable pageable) {
        return repo.findAll(pageable)
                .map(entity -> new ModelMapper().map(entity, CandidateProfileDTO.class));
    }

    @Override
    public CandidateProfileDTO updateCandidate(String externalId, CandidateProfileDTO dto) {
        CandidateProfile entity = repo.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));
        new ModelMapper().map(dto, entity);
        return new ModelMapper().map(repo.save(entity), CandidateProfileDTO.class);
    }

    @Override
    public void deleteCandidate(String externalId) {
        repo.deleteByExternalId(externalId);
    }
}

