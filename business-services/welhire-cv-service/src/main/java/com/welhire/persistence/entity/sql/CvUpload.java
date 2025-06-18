package com.welhire.persistence.entity.sql;

import com.welhire.shared.dto.audits.BaseEntity;
import com.welhire.shared.dto.enums.CandidateStatus;
import com.welhire.shared.dto.enums.ParseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cv_upload")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CvUpload extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String fileHash;
    private String cvName;
    private String filePath;
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private ParseStatus parseStatus;
    private String parseErrorMessage;
    private Instant parseStartTime;
    private Instant parseEndTime;

    @Enumerated(EnumType.STRING)
    private CandidateStatus candidateCreateStatus;
    private String candidateCreateErrorMessage;
    private Instant candidateCreateStartTime;
    private Instant candidateCreateEndTime;



    private String candidateId;


}