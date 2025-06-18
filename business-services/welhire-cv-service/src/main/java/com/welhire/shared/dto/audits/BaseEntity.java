package com.welhire.shared.dto.audits;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
 
import java.time.Instant;
 
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

 
    @CreatedBy
    private String createdBy;
    private String updatedBy;
 
    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;
 
    @LastModifiedDate
    private Instant updatedAt;
}