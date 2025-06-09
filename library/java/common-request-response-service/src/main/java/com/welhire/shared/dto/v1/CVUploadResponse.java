package com.welhire.shared.dto.v1;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CVUploadResponse {
    private String cvUploadId;
    private String fileName;
    private String savedPath;
    private boolean success;
    private String message;
    private boolean isParsed;


    public CVUploadResponse(String cvUploadId, String fileName, String savedPath, Boolean isParsed, String message) {
        this.cvUploadId=cvUploadId;
        this.fileName=fileName;
        this.savedPath=savedPath;
        this.isParsed=isParsed;
        this.message=message;
     }
}
