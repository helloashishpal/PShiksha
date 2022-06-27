package com.example.pshiksha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pshiksha.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.continueBtn.setOnClickListener {
            var phoneNumber = binding.phoneNumberEditText.text.toString().trim()
            if (phoneNumber.length < 10) {
                Toast.makeText(this, "Please Enter a valid Mobile Number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            var intent = Intent(applicationContext, OtpActivity::class.java)
            intent.putExtra(Util.INTENT_EXTRA_PHONE_NUMBER, phoneNumber);
            startActivity(intent)
        }
    }
}