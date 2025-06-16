package com.welhire.shared.dto.v1;


import lombok.Data;

@Data
public class CreateCandidateRequest {
    public String jdContentId;
    public String cvParsedId;
    public String cvUploadId;
    public String jdCvParsedId;  // can be a fixed value for now

    public CreateCandidateRequest() {}

    public CreateCandidateRequest(
            String jdContentId,
            String cvParsedId,
            String cvUploadId,
            String jdCvParsedId
    ) {
        this.jdContentId   = jdContentId;
        this.cvParsedId    = cvParsedId;
        this.cvUploadId    = cvUploadId;
        this.jdCvParsedId  = jdCvParsedId;
    }
}
