package com.welhire.cv.mocks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.welhire.persistence.entity.mongo.ParsedCandidateCV;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/parse")
public class MockParsingController {

    @Data
    public static class ParseRequest {
        public String jdContentId;
        public String fileName;
    }

    @PostMapping
    public ParsedCandidateCV parseCV(@RequestBody ParseRequest req) {
        log.info("Received parse request → jdId={} fileName={}", req.jdContentId, req.fileName);

        // simulate delay
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Sleep interrupted", e);
        }

        log.info("Delay complete; returning mock ParsedCandidateCV");

        return ParsedCandidateCV.builder()
                // super/Audit fields:


                // core parsed data:
                .cvUploadRefId(req.fileName)
                .name("MITI BHARDWAJ")
                .mobileNo("9033887718")
                .emailId("miti07.bhardwaj@gmail.com")
                .dateOfBirth("19/05/1999")
                .age("20")
                .location("Ahmedabad, Gujarat, India")
                .gender("Male")
                .maritalStatus("Single")
                .languagesKnown(List.of(
                        "English | Advanced",
                        "Hindi | Native",
                        "Gujarati | Advanced"
                ))
                .degree(List.of("MBA-Marketing", "BE-Electrical"))
                .university(List.of(
                        "Gujarat Technological University",
                        "Maharaja Sayajirao University"
                ))
                .year("Data not found")
                .yearOfExperience("7 years 6 months")
                .companyCurrent("Mogli Labs India Pvt Ltd.- Moglix")
                .companyLast(List.of("Zomato ltd", "Kotak Mahindra Bank Ltd"))
                .project("Data not found")
                .softwareSkills("Data not found")
                .strengths(List.of(
                        "Key Account Management",
                        "Team management",
                        "Project Management",
                        "Business development",
                        "Strategic sales",
                        "Operations planning",
                        "P&L ownership",
                        "Negotiation skills",
                        "Sales forecasting",
                        "Strategic planning"
                ))
                .summaryBrief(
                        "Miti Bhardwaj is a Senior Manager in Business Development and Account "
                                + "Management based in Ahmedabad, Gujarat, India. She has a proven track record…"
                )

                // metadata fields
                .dataEntryDate("24/05/2024")
                .userHashkey("-7471411372093908624")
                .emailReminderCnt(0)
                .linkStatus("Active")
                .dataUpdatedDate(List.of(Instant.parse("2024-09-30T14:49:52.991Z")))
                .tokenCountUpdated(List.of(940))

                // timestamp when this mock was parsed
                .parsedAt(Instant.now())
                .build();
    }
}

