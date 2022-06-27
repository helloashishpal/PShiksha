package com.example.pshiksha

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var phoneNumber: String
    private var otpTimeout = 60L
    private lateinit var storedVerificationId: String
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        phoneNumber = "+91" + intent.getStringExtra(Util.INTENT_EXTRA_PHONE_NUMBER).toString()

        binding.otpTitle2TextView.text = "Code is sent to $phoneNumber"


        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(
                    applicationContext,
                    "sign in started",
                    Toast.LENGTH_SHORT
                )
                    .show()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Toast.makeText(
                    applicationContext,
                    "Code sent to mobile Number +91 $phoneNumber.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                storedVerificationId = verificationId
            }
        }

        var phoneAuthOptions: PhoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(otpTimeout, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)

        binding.verifyBtn.setOnClickListener { v ->
            val code = binding.otpEditText.text.toString().trim()
            val phoneAuthCredential =
                PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(phoneAuthCredential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Successfully Signed In.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Sign In Failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}