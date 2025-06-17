package com.welhire.persistence.entity.sql;
import com.welhire.shared.dto.audits.BaseAudit;
import com.welhire.shared.dto.enums.ParseStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jd_cv_mapping")
@Data
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class JdCvMapping extends BaseAudit {
    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String jdRefId;
    private UUID cvUploadRefId;

}
