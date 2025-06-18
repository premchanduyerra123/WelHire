package com.welhire.shared.dto.v1;


import com.welhire.shared.dto.enums.ParseStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CVUploadResponse {
    private UUID cvUploadId;
    private String fileName;
    private String savedPath;
    private boolean success;
    private String message;
    private ParseStatus isParsed;


    public CVUploadResponse(UUID cvUploadId, String fileName, String savedPath, ParseStatus isParsed, String message) {
        this.cvUploadId=cvUploadId;
        this.fileName=fileName;
        this.savedPath=savedPath;
        this.isParsed=isParsed;
        this.message=message;
     }
}
