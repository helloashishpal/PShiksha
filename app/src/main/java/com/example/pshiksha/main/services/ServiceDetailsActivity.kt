package com.example.pshiksha.main.services

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.databinding.ActivityMajorMinorProjectBinding
import com.example.pshiksha.main.services.place_order.PlaceOrderActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ServiceDetailsActivity : AppCompatActivity() {
    var binding: ActivityMajorMinorProjectBinding? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var serviceItem: ServiceItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorMinorProjectBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        serviceItem = intent.getSerializableExtra("SERVICE") as ServiceItem?
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = serviceItem!!.title + " Service"
        binding!!.serviceImageView.setImageResource(serviceItem!!.imageResId)
        binding!!.serviceTitleTextView.text = serviceItem!!.title
        binding!!.serviceDescriptionTextView.text = serviceItem!!.description

        binding!!.onCallClickButton.setOnClickListener {
            onBtnClicked()
        }
    }

    private fun onBtnClicked() {
        val intent = Intent(applicationContext, PlaceOrderActivity::class.java)
        intent.putExtra("SERVICE", serviceItem)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}