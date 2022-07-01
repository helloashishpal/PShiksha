package com.example.pshiksha.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.pshiksha.R;
import com.example.pshiksha.databinding.ActivityTempBinding;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TempActivity extends AppCompatActivity {

    ActivityTempBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTempBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<Services> servicesList = new ArrayList<>();
        servicesList.add(new Services("TITLE1", R.drawable.ic_person_24, Color.RED));
        servicesList.add(new Services("TITLE2", R.drawable.ic_person_24, Color.RED));
        servicesList.add(new Services("TITLE3", R.drawable.ic_person_24, Color.RED));
        servicesList.add(new Services("TITLE4", R.drawable.ic_person_24, Color.RED));
        ServicesRecyclerView adapter = new ServicesRecyclerView(this, servicesList);
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);
    }
}