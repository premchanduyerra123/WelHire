package com.welhire.shared.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateCreationResponse {
    private String candidateId;
    private String status;
    private String message;
}
