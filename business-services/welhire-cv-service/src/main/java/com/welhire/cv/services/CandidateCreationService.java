package com.welhire.cv.services;

import com.welhire.cv.client.CandidateClient;
import com.welhire.exceptions.CandidateCreationException;
import com.welhire.persistence.entity.ParsedCandidateCV;
import com.welhire.shared.dto.v1.CandidateCreationResponse;
import com.welhire.shared.dto.v1.CreateCandidateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateCreationService {
    private final CandidateClient client;

    public CandidateCreationResponse createCandidate(ParsedCandidateCV pcv) {
        CreateCandidateRequest req = new CreateCandidateRequest(
                pcv.getJdContentId(),
                pcv.getId(),
                pcv.getCvUploadId(),
                pcv.getCandidateId()
        );
        try {
            return client.createCandidate(req);
        } catch (Exception ex) {
            throw new CandidateCreationException("Failed to create candidate", ex);
        }
    }
}
