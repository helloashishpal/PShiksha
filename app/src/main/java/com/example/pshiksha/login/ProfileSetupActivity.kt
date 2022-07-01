package com.example.pshiksha.login;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pshiksha.R;
import com.example.pshiksha.databinding.ActivityProfileSetupBinding;
import com.example.pshiksha.utils.LoaderBuilder;
import com.example.pshiksha.utils.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfileSetupActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    ActivityProfileSetupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        String[] college_names = getResources().getStringArray(R.array.college_names);
        String[] college_degree = getResources().getStringArray(R.array.college_degree);
        String[] college_branch = getResources().getStringArray(R.array.college_branch);
        String[] college_graduation_year = getResources().getStringArray(R.array.college_graduation_year);

        ArrayAdapter<String> college_names_adapter = new ArrayAdapter<>(this, R.layout.dropdown_layout, college_names);
        ArrayAdapter<String> college_degree_adapter = new ArrayAdapter<>(this, R.layout.dropdown_layout, college_degree);
        ArrayAdapter<String> college_branch_adapter = new ArrayAdapter<>(this, R.layout.dropdown_layout, college_branch);
        ArrayAdapter<String> college_graduation_year_adapter = new ArrayAdapter<>(this, R.layout.dropdown_layout, college_graduation_year);

        binding.collegeNameAutoCompleteTextView.setAdapter(college_names_adapter);
        binding.degreeAutoCompleteTextView.setAdapter(college_degree_adapter);
        binding.branchAutoCompleteTextView.setAdapter(college_branch_adapter);
        binding.graduationYearAutoCompleteTextView.setAdapter(college_graduation_year_adapter);

        //Filling phoneNumber and disabling phoneEditText
        binding.phoneNumberEditText.setText(currentUser.getPhoneNumber());
        binding.phoneNumberEditText.setEnabled(false);


        binding.confirmButton.setOnClickListener(view -> {
            UserInformation userInformation = getUserInformation();
            if (userInformation != null) {
                LoaderBuilder loaderBuilder = new LoaderBuilder(this);
                loaderBuilder.setTitle("Saving Information...");
                loaderBuilder.show();
                firebaseDatabase.getReference()
                        .child(Util.FIREBASE_USER_PROFILE_INFORMATION)
                        .child(currentUser.getUid())
                        .setValue(userInformation)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, getString(R.string.user_profile_success), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, getString(R.string.user_profile_failed), Toast.LENGTH_SHORT).show();
                            }
                            loaderBuilder.hide();
                        });
            }
        });
    }

    private UserInformation getUserInformation() {
        String fullName = Objects.requireNonNull(binding.fullNameEditText.getText()).toString().trim();
        String phoneNumber = Objects.requireNonNull(binding.phoneNumberEditText.getText()).toString().trim();
        String collegeName = Objects.requireNonNull(binding.collegeNameAutoCompleteTextView.getText()).toString().trim();
        String collegeDegree = Objects.requireNonNull(binding.degreeAutoCompleteTextView.getText()).toString().trim();
        String collegeBranch = Objects.requireNonNull(binding.branchAutoCompleteTextView.getText()).toString().trim();
        String collegeGraduationYear = Objects.requireNonNull(binding.graduationYearAutoCompleteTextView.getText()).toString().trim();

        if (fullName.isEmpty()) {
            binding.fullNameEditTextLayout.setError("Please enter a valid Name.");
            binding.fullNameEditText.requestFocus();
            return null;
        } else if (phoneNumber.isEmpty()) {
            binding.phoneNumberEditTextLayout.setError("Please enter a valid Phone Number.");
            binding.phoneNumberEditText.requestFocus();
            return null;
        } else if (collegeName.isEmpty()) {
            binding.collegeNameAutoCompleteTextViewLayout.setError("Please select a College.");
            binding.collegeNameAutoCompleteTextView.requestFocus();
            return null;
        } else if (collegeDegree.isEmpty()) {
            binding.degreeAutoCompleteTextViewLayout.setError("Please select a Degree.");
            binding.degreeAutoCompleteTextView.requestFocus();
            return null;
        } else if (collegeBranch.isEmpty()) {
            binding.branchAutoCompleteTextViewLayout.setError("Please select a Branch.");
            binding.branchAutoCompleteTextView.requestFocus();
            return null;
        } else if (collegeGraduationYear.isEmpty()) {
            binding.graduationYearAutoCompleteTextViewLayout.setError("Please select a Graduation year.");
            binding.graduationYearAutoCompleteTextView.requestFocus();
            return null;
        }
        return new UserInformation(fullName, phoneNumber, collegeName, collegeDegree, collegeBranch, collegeGraduationYear);
    }
}