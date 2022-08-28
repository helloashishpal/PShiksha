package com.pshiksha4.pshiksha.login

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pshiksha4.pshiksha.R
import com.pshiksha4.pshiksha.databinding.ActivityOtpBinding
import com.pshiksha4.pshiksha.main.MainActivity
import com.pshiksha4.pshiksha.utils.LoaderBuilder
import com.pshiksha4.pshiksha.utils.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.concurrent.TimeUnit


class OtpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityOtpBinding

    private lateinit var phoneNumber: String
    private var otpTimeout = 60L
    private var canSend = false

    private lateinit var storedVerificationId: String
    private lateinit var mToken: ForceResendingToken

    private lateinit var loader: LoaderBuilder

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(
                applicationContext, e.toString(),
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

        override fun onCodeSent(
            verificationId: String,
            token: ForceResendingToken
        ) {
            val otpSuccessMessage = getString(R.string.otp_sent_success) + ' ' + phoneNumber
            Snackbar.make(
                binding.root,
                otpSuccessMessage,
                Snackbar.LENGTH_SHORT
            ).show()

            storedVerificationId = verificationId
            mToken = token
            binding.otpView.requestFocusOTP()
            loader.hide()
            object : CountDownTimer(otpTimeout * 1000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val message = "Resend after ${millisUntilFinished / 1000}"
                    binding.otpTitle4TextView.text = message
                }

                override fun onFinish() {
                    canSend = true
                    val message = "Resend OTP!"
                    binding.otpTitle4TextView.text = message
                }
            }.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        phoneNumber = intent.getStringExtra(Util.INTENT_EXTRA_PHONE_NUMBER).toString()
        val otpMessage = "OTP sent to $phoneNumber"
        binding.otpTitle2TextView.text = otpMessage

        //[Listeners Start]
        binding.homeUpButton.setOnClickListener {
            onBackPressed()
        }

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

        binding.otpTitle4TextView.setOnClickListener {
            resendVerificationCode()
        }
        //[Listeners End]

        sendVerificationCode()
    }

    private fun sendVerificationCode() {
        canSend = false
        loader = LoaderBuilder(this).setTitle("Sending OTP...")
        loader.show()

        val phoneAuthOptions: PhoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(otpTimeout, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val signInLoader = LoaderBuilder(this).setTitle("Signing In...")
        signInLoader.show()

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.otpView.showSuccess()
                    onSignInSuccess()
                } else {
                    onSignInFailed()
                }
                signInLoader.hide()
            }
    }

    private fun resendVerificationCode() {
        canSend = false
        loader = LoaderBuilder(this).setTitle("Sending OTP...")
        loader.show()
        val phoneAuthOptions: PhoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(otpTimeout, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(mToken)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    private fun onSignInFailed() {
        Toast.makeText(
            applicationContext,
            getString(R.string.otp_incorrect),
            Toast.LENGTH_SHORT
        ).show()
        binding.otpView.showError()
        loader.hide()
    }

    private fun onSignInSuccess() {
        startActivity(
            Intent(
                applicationContext,
                MainActivity::class.java
            )
        )
        loader.hide()
    }
}