package com.welhire.persistence.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "parsed_candidate_cv")
@Data
public class ParsedCandidateCV {
    @Id
    private String id;
    private String jdContentId;
    private String cvUploadId;
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
    private String key;
    private String jdid;
    private String dataEntryDate;
    private String userHashkey;
    private Integer emailReminderCnt;
    private String linkStatus;
    private List<Instant> dataUpdatedDate;
    private List<Integer> tokenCountUpdated;
    private Instant parsedAt = Instant.now();

    @SuppressWarnings("unchecked")
    public static ParsedCandidateCV fromMap(Map<String,Object> m) {
        ParsedCandidateCV p = new ParsedCandidateCV();
        p.setJdContentId((String)   m.get("jdContentId"));
        p.setName((String)           m.get("name"));
        p.setMobileNo((String)       m.get("mobileNo"));
        p.setEmailId((String)        m.get("emailId"));
        p.setDateOfBirth((String)    m.get("dateOfBirth"));
        p.setAge((String)            m.get("age"));
        p.setLocation((String)       m.get("location"));
        p.setGender((String)         m.get("gender"));
        p.setMaritalStatus((String)  m.get("maritalStatus"));
        p.setLanguagesKnown((List<String>) m.get("languagesKnown"));
        p.setDegree((List<String>)         m.get("degree"));
        p.setUniversity((List<String>)     m.get("university"));
        p.setYear((String)                 m.get("year"));
        p.setYearOfExperience((String)     m.get("yearOfExperience"));
        p.setCompanyCurrent((String)       m.get("companyCurrent"));
        p.setCompanyLast((List<String>)    m.get("companyLast"));
        p.setProject((String)              m.get("project"));
        p.setSoftwareSkills((String)       m.get("softwareSkills"));
        p.setStrengths((List<String>)      m.get("strengths"));
        p.setSummaryBrief((String)         m.get("summaryBrief"));

        p.setKey((String)                  m.get("key"));
        p.setJdid((String)                 m.get("jdid"));
        p.setDataEntryDate((String)        m.get("dataEntryDate"));
        p.setUserHashkey((String)          m.get("userHashkey"));
        p.setEmailReminderCnt((Integer)    m.get("emailReminderCnt"));
        p.setLinkStatus((String)           m.get("linkStatus"));

        // convert list-of-{ "$date":string } into Instant
        List<Map<String,String>> dates =
                (List<Map<String,String>>) m.get("dataUpdatedDate");
        if (dates != null) {
            p.setDataUpdatedDate(
                    dates.stream()
                            .map(d -> Instant.parse(d.get("$date")))
                            .toList()
            );
        }

        p.setTokenCountUpdated((List<Integer>) m.get("tokenCountUpdated"));
        p.setParsedAt(Instant.now());
        return p;
    }}

