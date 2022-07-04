package com.example.pshiksha.services.AllServices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.pshiksha.R;
import com.example.pshiksha.databinding.ActivityMajorMinorProjectBinding;

public class MajorMinorProjectActivity extends AppCompatActivity {

    ActivityMajorMinorProjectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMajorMinorProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.onCallClickButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.pshiksha_phone_number)));
            startActivity(intent);
        });

    }
}