package com.welhire.cv.services;

import com.welhire.cv.client.CandidateClient;
import com.welhire.exceptions.CandidateCreationException;
import com.welhire.persistence.entity.mongo.CvParsed;
import com.welhire.shared.dto.v1.CandidateCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateCreationService {
    private final CandidateClient client;

    public CandidateCreationResponse createCandidate(CvParsed pcv) {
        try {
            return client.createCandidate(pcv);
        } catch (Exception ex) {
            throw new CandidateCreationException("Failed to create candidate", ex);
        }
    }
}
