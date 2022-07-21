package com.example.pshiksha.login

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.R
import com.example.pshiksha.databinding.ActivityProfileSetupBinding
import com.example.pshiksha.main.MainActivity
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ProfileSetupActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var currentUser: FirebaseUser? = null
    private var binding: ActivityProfileSetupBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        currentUser = firebaseAuth!!.currentUser

        initDropdowns()

        //Filling phoneNumber and disabling phoneEditText
        binding!!.phoneNumberEditText.setText(currentUser!!.phoneNumber)
        binding!!.phoneNumberEditText.isEnabled = false


        binding!!.confirmButton.setOnClickListener {
            val userInformation = userInformation
            if (userInformation != null) {
                val loaderBuilder = LoaderBuilder(this)
                loaderBuilder.setTitle("Saving Information...")
                loaderBuilder.show()
                firebaseDatabase!!.reference
                    .child(Util.FIREBASE_USER_PROFILE_INFORMATION)
                    .child(currentUser!!.uid)
                    .setValue(userInformation)
                    .addOnCompleteListener { task: Task<Void?> ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                getString(R.string.user_profile_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    applicationContext,
                                    MainActivity::class.java
                                )
                            )
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.user_profile_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        loaderBuilder.hide()
                    }
            }
        }
    }

    private fun initDropdowns() {
//        val collegeNames = resources.getStringArray(R.array.college_names)
        val collegeDegree = resources.getStringArray(R.array.college_degree)
//        val collegeBranch = resources.getStringArray(R.array.college_branch)
        val collegeGraduationYear = resources.getStringArray(R.array.college_graduation_year)
//        val collegeNamesAdapter = ArrayAdapter(this, R.layout.dropdown_layout, collegeNames)
        val collegeDegreeAdapter = ArrayAdapter(this, R.layout.dropdown_layout, collegeDegree)
//        val collegeBranchAdapter = ArrayAdapter(this, R.layout.dropdown_layout, collegeBranch)
        val collegeGraduationYearAdapter =
            ArrayAdapter(this, R.layout.dropdown_layout, collegeGraduationYear)
//        binding!!.collegeNameAutoCompleteTextView.setAdapter(collegeNamesAdapter)
        binding!!.degreeAutoCompleteTextView.setAdapter(collegeDegreeAdapter)
//        binding!!.branchAutoCompleteTextView.setAdapter(collegeBranchAdapter)
        binding!!.graduationYearAutoCompleteTextView.setAdapter(collegeGraduationYearAdapter)
    }

    private val userInformation: UserInformation?
        get() {
            val fullName = Objects.requireNonNull(binding!!.fullNameEditText.text).toString()
                .trim { it <= ' ' }
            val phoneNumber = Objects.requireNonNull(binding!!.phoneNumberEditText.text).toString()
                .trim { it <= ' ' }
            val collegeName =
                Objects.requireNonNull(binding!!.collegeNameAutoCompleteTextView.text).toString()
                    .trim { it <= ' ' }
            val collegeDegree =
                Objects.requireNonNull(binding!!.degreeAutoCompleteTextView.text).toString()
                    .trim { it <= ' ' }
            val collegeBranch =
                Objects.requireNonNull(binding!!.branchAutoCompleteTextView.text).toString()
                    .trim { it <= ' ' }
            val collegeGraduationYear = Objects.requireNonNull(
                binding!!.graduationYearAutoCompleteTextView.text
            ).toString().trim { it <= ' ' }
            if (fullName.isEmpty()) {
                binding!!.fullNameEditTextLayout.error = "Please enter a valid Name."
                binding!!.fullNameEditText.requestFocus()
                return null
            } else if (phoneNumber.isEmpty()) {
                binding!!.phoneNumberEditTextLayout.error = "Please enter a valid Phone Number."
                binding!!.phoneNumberEditText.requestFocus()
                return null
            } else if (collegeName.isEmpty()) {
                binding!!.collegeNameAutoCompleteTextViewLayout.error = "Please select a College."
                binding!!.collegeNameAutoCompleteTextView.requestFocus()
                return null
            } else if (collegeDegree.isEmpty()) {
                binding!!.degreeAutoCompleteTextViewLayout.error = "Please select a Degree."
                binding!!.degreeAutoCompleteTextView.requestFocus()
                return null
            } else if (collegeBranch.isEmpty()) {
                binding!!.branchAutoCompleteTextViewLayout.error = "Please select a Branch."
                binding!!.branchAutoCompleteTextView.requestFocus()
                return null
            } else if (collegeGraduationYear.isEmpty()) {
                binding!!.graduationYearAutoCompleteTextViewLayout.error =
                    "Please select a Graduation year."
                binding!!.graduationYearAutoCompleteTextView.requestFocus()
                return null
            }
            return UserInformation(
                fullName,
                phoneNumber,
                collegeName,
                collegeDegree,
                collegeBranch,
                collegeGraduationYear,
                false
            )
        }
}