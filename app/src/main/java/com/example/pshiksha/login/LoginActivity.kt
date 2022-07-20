package com.example.pshiksha.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pshiksha.R
import com.example.pshiksha.utils.Util
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
                binding.phoneNumberEditTextLayout.error = getString(R.string.invalid_phone_number)
                return@setOnClickListener
            }
            phoneNumber = binding.countryCodeEditText.text.toString() + phoneNumber

            val intent = Intent(applicationContext, OtpActivity::class.java)
            intent.putExtra(Util.INTENT_EXTRA_PHONE_NUMBER, phoneNumber)
            startActivity(intent)
        }
    }
}