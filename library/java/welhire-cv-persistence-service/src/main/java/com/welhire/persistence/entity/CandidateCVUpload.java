package com.welhire.persistence.entity;

 import jakarta.persistence.Id;
 import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.springframework.data.mongodb.core.mapping.Document;

 import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidate_cv_uploads")
public class CandidateCVUpload {

    @Id
    private String id;                    // previously UUID
    private String userEmail;
    private String jdContentId;
    private String cvName;
    private String filePath;
    private Boolean isParsed = false;
    private LocalDateTime parseStartTime;
    private LocalDateTime parseEndTime;
    private LocalDateTime createdAt = LocalDateTime.now();

 }


