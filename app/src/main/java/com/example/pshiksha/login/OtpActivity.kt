package com.example.pshiksha.login

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.R
import com.example.pshiksha.databinding.ActivityOtpBinding
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit


class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var phoneNumber: String
    private var otpTimeout = 1L
    private lateinit var storedVerificationId: String
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        phoneNumber = intent.getStringExtra(Util.INTENT_EXTRA_PHONE_NUMBER).toString()
        val otpMessage = "OTP sent to $phoneNumber"
        binding.otpTitle2TextView.text = otpMessage


        val loader = LoaderBuilder(this)
            .setTitle("Sending OTP...")
        loader.show()

        binding.homeUpButton.setOnClickListener {
            onBackPressed()
        }


        val phoneAuthOptions: PhoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(otpTimeout, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    val otpSuccessMessage = getString(R.string.otp_sent_success) + ' ' + phoneNumber
                    Snackbar.make(
                        binding.root,
                        otpSuccessMessage,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    storedVerificationId = verificationId
                    binding.otpView.requestFocusOTP()
                    loader.hide()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)

        binding.verifyBtn.setOnClickListener {
            val otp = binding.otpView.otp.toString().trim()
            if (otp.isEmpty()) {
                binding.otpView.showError()
                return@setOnClickListener
            }
            val phoneAuthCredential =
                PhoneAuthProvider.getCredential(storedVerificationId, otp)
            signInWithPhoneAuthCredential(phoneAuthCredential)
        }

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
            }

            override fun onOTPComplete(otp: String) {
                val phoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val signInLoader = LoaderBuilder(this).setTitle("Signing In...")
        signInLoader.show()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.otpView.showSuccess()
                    signInLoader.hide()

                    val loaderBuilder = LoaderBuilder(this@OtpActivity)
                    loaderBuilder.setTitle("Checking Information...")
                    loaderBuilder.show()


                    val currentUserUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    if (currentUserUid.isNotEmpty()) {
                        val rootRef = FirebaseDatabase.getInstance()
                            .getReference(Util.FIREBASE_USER_PROFILE_INFORMATION)
                            .child(currentUserUid)
                        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    //Profile Exists.
                                    Toast.makeText(
                                        applicationContext,
                                        "Profile Exists",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    //Profile does not exists. open a ProfileSetupActivity
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            ProfileSetupActivity::class.java
                                        )
                                    )
                                }
                                loaderBuilder.hide()
                                finishAffinity()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.something_went_wrong),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                                finishAffinity()
                            }
                        })
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.otp_incorrect),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.otpView.showError()
                }
                signInLoader.hide()
            }
    }
}