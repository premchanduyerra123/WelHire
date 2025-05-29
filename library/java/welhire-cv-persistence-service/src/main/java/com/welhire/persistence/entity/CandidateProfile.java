package com.welhire.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true)
    private String externalId; // use key or JDID

    private String name;
    private String mobileNo;
    private String emailId;
    private String dateOfBirth;
    private String age;
    private String location;
    private String gender;
    private String maritalStatus;
    private String languagesKnown;
    private String degree;
    private String university;
    private String year;
    private String yearOfExperience;
    private String companyCurrent;
    private String companyLast;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String summaryBrief;

    private String keyField;
    private String jdid;
    private String dataEntryDate;
    private String userHashkey;
    private String linkStatus;
    private String emailReminderCnt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getLanguagesKnown() {
        return languagesKnown;
    }

    public void setLanguagesKnown(String languagesKnown) {
        this.languagesKnown = languagesKnown;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(String yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    public String getCompanyCurrent() {
        return companyCurrent;
    }

    public void setCompanyCurrent(String companyCurrent) {
        this.companyCurrent = companyCurrent;
    }

    public String getCompanyLast() {
        return companyLast;
    }

    public void setCompanyLast(String companyLast) {
        this.companyLast = companyLast;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getSummaryBrief() {
        return summaryBrief;
    }

    public void setSummaryBrief(String summaryBrief) {
        this.summaryBrief = summaryBrief;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    public String getJdid() {
        return jdid;
    }

    public void setJdid(String jdid) {
        this.jdid = jdid;
    }

    public String getDataEntryDate() {
        return dataEntryDate;
    }

    public void setDataEntryDate(String dataEntryDate) {
        this.dataEntryDate = dataEntryDate;
    }

    public String getUserHashkey() {
        return userHashkey;
    }

    public void setUserHashkey(String userHashkey) {
        this.userHashkey = userHashkey;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getEmailReminderCnt() {
        return emailReminderCnt;
    }

    public void setEmailReminderCnt(String emailReminderCnt) {
        this.emailReminderCnt = emailReminderCnt;
    }
}

