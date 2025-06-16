package com.welhire.persistence.entity;

 import com.welhire.shared.dto.audits.BaseAudit;
 import com.welhire.shared.dto.enums.ParseStatus;
 import jakarta.persistence.Id;
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.springframework.data.mongodb.core.mapping.Document;

 import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidate_cv_uploads")
@Builder
public class CandidateCVUpload extends BaseAudit {

    @Id
    private String id;
    private String userEmail;
    private String jdRefId;
    private String cvName;
    private String filePath;
    private String fileHash;
    private ParseStatus parseStatus = ParseStatus.CV_UPLOADED;
    private String candidateId;
    private String parseErrorMessage;
    private LocalDateTime parseStartTime;
    private LocalDateTime parseEndTime;

 }


