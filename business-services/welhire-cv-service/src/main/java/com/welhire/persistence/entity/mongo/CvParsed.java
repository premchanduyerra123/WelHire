package com.welhire.persistence.entity.mongo;

import com.welhire.shared.dto.audits.BaseAudit;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "cv_parsed")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CvParsed extends BaseAudit {
    @Id
    private String id;
    private UUID cvUploadRefId;
    private String name;
    private String mobileNo;
    private String emailId;
    private String dateOfBirth;
    private String age;
    private String location;
    private String gender;
    private String maritalStatus;
    private List<String> languagesKnown;
    private List<String> degree;
    private List<String> university;
    private String year;
    private String yearOfExperience;
    private String companyCurrent;
    private List<String> companyLast;
    private String project;
    private String softwareSkills;
    private List<String> strengths;
    private String summaryBrief;
    private String dataEntryDate;
    private String userHashkey;
    private Integer emailReminderCnt;
    private String linkStatus;
    private List<Instant> dataUpdatedDate;
    private List<Integer> tokenCountUpdated;
    private Instant parsedAt = Instant.now();




}

