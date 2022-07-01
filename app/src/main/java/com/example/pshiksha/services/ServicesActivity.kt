package com.example.pshiksha

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pshiksha.databinding.ActivityServicesBinding
import com.example.pshiksha.services.Services
import com.example.pshiksha.services.ServicesRecyclerView

class ServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServicesBinding
    private val servicesList: MutableList<Services> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        servicesList.add(Services("TITLE1", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE2", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, Color.RED))
        servicesList.add(Services("TITLE4", R.drawable.ic_person_24, Color.RED))
        val adapter = ServicesRecyclerView(this, servicesList)
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { position: Int ->
            Toast.makeText(
                this,
                "POSITION : $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}