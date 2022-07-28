package com.example.pshiksha.login;

import java.io.Serializable;

public class UserInformation implements Serializable {
    private String fullName;
    private String phoneNumber;
    private String collegeName;
    private String collegeDegree;
    private String collegeBranch;
    private String collegeGraduationYear;
    private String referralCode;
    private Boolean admin;

    public UserInformation() {
    }

    public UserInformation(String fullName, String phoneNumber, String collegeName, String collegeDegree, String collegeBranch, String collegeGraduationYear, String referralCode, Boolean admin) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.collegeName = collegeName;
        this.collegeDegree = collegeDegree;
        this.collegeBranch = collegeBranch;
        this.collegeGraduationYear = collegeGraduationYear;
        this.referralCode = referralCode;
        this.admin = admin;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getCollegeDegree() {
        return collegeDegree;
    }

    public String getCollegeBranch() {
        return collegeBranch;
    }

    public String getCollegeGraduationYear() {
        return collegeGraduationYear;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public Boolean getAdmin() {
        return admin;
    }
}
