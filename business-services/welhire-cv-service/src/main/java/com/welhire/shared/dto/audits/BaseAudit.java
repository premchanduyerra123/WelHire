package com.welhire.shared.dto.audits;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseAudit {

    private String createdBy;
    private LocalDateTime createdAt;

    private String updatedBy;
    private LocalDateTime updatedAt;
}