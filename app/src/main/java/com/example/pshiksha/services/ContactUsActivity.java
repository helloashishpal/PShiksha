package com.example.pshiksha.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.pshiksha.R;
import com.example.pshiksha.databinding.ActivityContactUsBinding;

import java.util.Locale;
import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    private ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.homeUpButton.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.contactUsPhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.pshiksha_phone_number)));
            startActivity(intent);
        });

        binding.contactUsEmail.setOnClickListener(v -> {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.pshiksha_email_id)});
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        });

        binding.contactUsWhatsapp.setOnClickListener(v -> {
            PackageManager packageManager = getPackageManager();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            try {
                intent.setPackage("com.whatsapp");
                String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.pshiksha_phone_number) + "&text=";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        binding.locateOnMap.setOnClickListener(v -> {
            float latitude = 28.737377345535823f;
            float longitude = 77.1163373952647f;
            String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
    }
}