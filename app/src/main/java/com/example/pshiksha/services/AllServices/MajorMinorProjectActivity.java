package com.example.pshiksha.services.AllServices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pshiksha.databinding.ActivityMajorMinorProjectBinding;

public class MajorMinorProjectActivity extends AppCompatActivity {

    ActivityMajorMinorProjectBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMajorMinorProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding

    }
}