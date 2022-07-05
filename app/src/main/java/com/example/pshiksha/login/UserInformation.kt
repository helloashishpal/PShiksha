package com.example.pshiksha.login

import java.io.Serializable

class UserInformation : Serializable {
    var fullName: String? = null
        private set
    var phoneNumber: String? = null
        private set
    var collegeName: String? = null
        private set
    var collegeDegree: String? = null
        private set
    var collegeBranch: String? = null
        private set
    var collegeGraduationYear: String? = null
        private set

    constructor() {}
    constructor(
        fullName: String?,
        phoneNumber: String?,
        collegeName: String?,
        collegeDegree: String?,
        collegeBranch: String?,
        collegeGraduationYear: String?
    ) {
        this.fullName = fullName
        this.phoneNumber = phoneNumber
        this.collegeName = collegeName
        this.collegeDegree = collegeDegree
        this.collegeBranch = collegeBranch
        this.collegeGraduationYear = collegeGraduationYear
    }
}