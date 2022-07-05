package com.example.pshiksha.services.AllServices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.pshiksha.R;
import com.example.pshiksha.databinding.ActivityMajorMinorProjectBinding;
import com.example.pshiksha.services.ServiceItem;

import java.util.Objects;

public class MajorMinorProjectActivity extends AppCompatActivity {

    ActivityMajorMinorProjectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMajorMinorProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ServiceItem serviceItem = (ServiceItem) getIntent().getSerializableExtra("SERVICE");
        getSupportActionBar().setTitle(serviceItem.getTitle() + " Service");
        binding.serviceImageView.setImageResource(serviceItem.getImageResId());
        binding.serviceTitleTextView.setText(serviceItem.getTitle());

        binding.onCallClickButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.pshiksha_phone_number)));
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}