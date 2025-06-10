package com.welhire.shared.dto.v1;


import com.welhire.shared.dto.enums.ParseStatus;
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
    private ParseStatus isParsed;


    public CVUploadResponse(String cvUploadId, String fileName, String savedPath, ParseStatus isParsed, String message) {
        this.cvUploadId=cvUploadId;
        this.fileName=fileName;
        this.savedPath=savedPath;
        this.isParsed=isParsed;
        this.message=message;
     }
}
