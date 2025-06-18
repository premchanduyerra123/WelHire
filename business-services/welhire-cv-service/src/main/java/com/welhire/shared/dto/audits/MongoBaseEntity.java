package com.welhire.shared.dto.audits;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
 
import java.time.Instant;
 
 
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class MongoBaseEntity {

    private String createdBy;
    private String updatedBy;
 
    @CreatedDate
    private Instant createdAt;
 
    @LastModifiedDate
    private Instant updatedAt;
 
}