package com.example.pshiksha.login;

public class UserInformation {
    private final String fullName;
    private final String phoneNumber;
    private final String collegeName;
    private final String collegeDegree;
    private final String collegeBranch;
    private final String collegeGraduationYear;

    public UserInformation(String fullName, String phoneNumber, String collegeName, String collegeDegree, String collegeBranch, String collegeGraduationYear) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.collegeName = collegeName;
        this.collegeDegree = collegeDegree;
        this.collegeBranch = collegeBranch;
        this.collegeGraduationYear = collegeGraduationYear;
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
}
