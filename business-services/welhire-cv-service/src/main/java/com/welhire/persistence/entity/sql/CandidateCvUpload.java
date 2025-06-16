package com.welhire.persistence.entity.sql;

import com.welhire.shared.dto.audits.BaseAudit;
import com.welhire.shared.dto.enums.ParseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_cv_upload")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCvUpload extends BaseAudit {
    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String userEmail;
    private String jdRefId;
    private String cvName;
    private String filePath;
    private String fileHash;
    private String candidateId;
    @Enumerated(EnumType.STRING)
    private ParseStatus parseStatus;
    private String parseErrorMessage;
    private LocalDateTime parseStartTime;
    private LocalDateTime parseEndTime;

}