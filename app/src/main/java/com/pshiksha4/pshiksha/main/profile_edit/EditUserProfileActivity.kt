package com.pshiksha4.pshiksha.main.profile_edit

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pshiksha4.pshiksha.R
import com.pshiksha4.pshiksha.databinding.ActivityEditUserProfileBinding
import com.pshiksha4.pshiksha.login.LoginActivity
import com.pshiksha4.pshiksha.login.UserInformation
import com.pshiksha4.pshiksha.utils.LoaderBuilder
import com.pshiksha4.pshiksha.utils.Util
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EditUserProfileActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var currentUser: FirebaseUser? = null
    private var binding: ActivityEditUserProfileBinding? = null
    private var currentUserInformation: UserInformation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        currentUser = firebaseAuth!!.currentUser

        currentUserInformation = intent.getSerializableExtra("USER_INFORMATION") as UserInformation?
        fillCurrentUserInformation()
        fillDropDowns()


        binding!!.confirmButton.setOnClickListener {
            val userInformation = userInformation
            if (userInformation != null) {
                val loaderBuilder = LoaderBuilder(this)
                loaderBuilder.setTitle("Saving Information...")
                loaderBuilder.show()
                firebaseDatabase!!.reference
                    .child(Util.FIREBASE_USERS)
                    .child(currentUser!!.uid)
                    .child(Util.FIREBASE_USER_INFORMATION)
                    .setValue(userInformation)
                    .addOnCompleteListener { task: Task<Void?> ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                getString(R.string.user_profile_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
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

        binding!!.homeUpButton.setOnClickListener {
            onBackPressed()
        }

        binding!!.logOutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log Out?")
                .setPositiveButton("Yes") { _, _ ->
                    firebaseAuth?.signOut()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finishAffinity()
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .show()
        }
    }

    private fun fillDropDowns() {
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

    private fun fillCurrentUserInformation() {
        binding!!.fullNameEditText.setText(currentUserInformation?.fullName)
        binding!!.phoneNumberEditText.setText(currentUserInformation?.phoneNumber)
        binding!!.collegeNameAutoCompleteTextView.setText(currentUserInformation?.collegeName)
        binding!!.degreeAutoCompleteTextView.setText(currentUserInformation?.collegeDegree)
        binding!!.branchAutoCompleteTextView.setText(currentUserInformation?.collegeBranch)
        binding!!.graduationYearAutoCompleteTextView.setText(currentUserInformation?.collegeGraduationYear)
    }

    private val userInformation: UserInformation?
        get() {
            val fullName = Objects.requireNonNull(binding!!.fullNameEditText.text).toString()
                .trim()
            val phoneNumber = Objects.requireNonNull(binding!!.phoneNumberEditText.text).toString()
                .trim()
            val collegeName =
                Objects.requireNonNull(binding!!.collegeNameAutoCompleteTextView.text).toString()
                    .trim()
            val collegeDegree =
                Objects.requireNonNull(binding!!.degreeAutoCompleteTextView.text).toString()
                    .trim()
            val collegeBranch =
                Objects.requireNonNull(binding!!.branchAutoCompleteTextView.text).toString()
                    .trim()
            val collegeGraduationYear = Objects.requireNonNull(
                binding!!.graduationYearAutoCompleteTextView.text
            ).toString().trim()
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
                userInformation?.referralCode,
                currentUserInformation?.admin
            )
        }


    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Are you sure to Exit")
            .setMessage("Any changes will not be saved.")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }
}