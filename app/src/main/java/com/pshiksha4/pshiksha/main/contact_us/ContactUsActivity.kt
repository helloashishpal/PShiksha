package com.pshiksha4.pshiksha.main.contact_us

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.pshiksha4.pshiksha.R
import android.net.Uri
import android.widget.Toast
import com.pshiksha4.pshiksha.databinding.ActivityContactUsBinding
import java.lang.Exception

class ContactUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactUsBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.homeUpButton.setOnClickListener { onBackPressed() }
        binding.contactUsPhoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + getString(R.string.pshiksha_phone_number))
            startActivity(intent)
        }
        binding.contactUsEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "plain/text"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.pshiksha_email_id)))
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        }
        binding.contactUsWhatsapp.setOnClickListener {
            val packageManager = packageManager
            val intent = Intent(Intent.ACTION_VIEW)
            try {
                intent.setPackage("com.whatsapp")
                val url =
                    "https://api.whatsapp.com/send?phone=" + getString(R.string.pshiksha_phone_number) + "&text="
                intent.data = Uri.parse(url)
                startActivity(intent)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Please install Whatsapp first.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
        binding.locateOnMap.setOnClickListener {
            val latitude = 28.737377345535823f
            val longitude = 77.1163373952647f
            val uri = "http://maps.google.com/maps?daddr=$latitude,$longitude"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
    }
}