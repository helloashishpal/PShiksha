package com.example.pshiksha.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.databinding.ActivitySplashScreenBinding
import com.example.pshiksha.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser

        binding.linearLayout.animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)

        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
                startActivity(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                )
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            finishAffinity()
        }, 1000)
    }
}