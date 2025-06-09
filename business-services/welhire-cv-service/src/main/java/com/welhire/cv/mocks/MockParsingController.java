package com.welhire.cv.mocks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@Slf4j
@RestController
@RequestMapping("/parse")
public class MockParsingController {

    public static class ParseRequest {
        public String jdContentId;
        public String fileName;
    }

    @PostMapping
    public Map<String,Object> parseCV(@RequestBody ParseRequest req) throws InterruptedException {
        // NOTE: you can adjust any of these values as “mock” data
        log.info("Received parse request → jdId={} fileName={}", req.jdContentId, req.fileName);

        log.info("Simulating delay of 10 seconds...");
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Sleep interrupted", e);
        }

        log.info("Delay complete; constructing mock response for jdId={}, fileName={}", req.jdContentId, req.fileName);

        return Map.ofEntries(
                Map.entry("jdContentId",  req.jdContentId),
                Map.entry("name",         "MITI BHARDWAJ"),
                Map.entry("mobileNo",     "9033887718"),
                Map.entry("emailId",      "miti07.bhardwaj@gmail.com"),
                Map.entry("dateOfBirth",  "Data not found"),
                Map.entry("age",          "Data not found"),
                Map.entry("location",     "Ahmedabad, Gujarat, India"),
                Map.entry("gender",       "Data not found"),
                Map.entry("maritalStatus","Data not found"),
                Map.entry("languagesKnown", List.of(
                        "English | Advanced",
                        "Hindi | Native",
                        "Gujarati | Advanced"
                )),
                Map.entry("degree",       List.of("MBA-Marketing", "BE-Electrical")),
                Map.entry("university",   List.of(
                        "Gujarat Technological University",
                        "Maharaja Sayajirao University"
                )),
                Map.entry("year",         "Data not found"),
                Map.entry("yearOfExperience", "7 years 6 months"),
                Map.entry("companyCurrent",   "Mogli Labs India Pvt Ltd.- Moglix"),
                Map.entry("companyLast",      List.of("Zomato ltd", "Kotak Mahindra Bank Ltd")),
                Map.entry("project",      "Data not found"),
                Map.entry("softwareSkills","Data not found"),
                Map.entry("strengths",    List.of(
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
                )),
                Map.entry("summaryBrief",
                        "Miti Bhardwaj is a Senior Manager in Business Development and Account "
                                + "Management based in Ahmedabad, Gujarat, India. She has a proven track record…"
                ),
                Map.entry("key", req.jdContentId + "_" + req.fileName),
                Map.entry("jdid", req.jdContentId),
                Map.entry("dataEntryDate", "24/05/2024"),
                Map.entry("userHashkey", "-7471411372093908624"),
                Map.entry("emailReminderCnt", 0),
                Map.entry("linkStatus", "Active"),
                Map.entry("dataUpdatedDate", List.of(
                        Map.of("$date", "2024-09-30T14:49:52.991Z")
                )),
                Map.entry("tokenCountUpdated", List.of(940))
        );
    }
}

