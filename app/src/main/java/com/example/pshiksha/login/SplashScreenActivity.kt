package com.example.pshiksha.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.databinding.ActivitySplashScreenBinding
import com.example.pshiksha.services.ServicesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreenActivity : AppCompatActivity() {
    var binding: ActivitySplashScreenBinding? = null
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent = if (user != null)
                Intent(applicationContext, ServicesActivity::class.java)
            else
                Intent(applicationContext, LoginActivity::class.java)
//            intent = new Intent(this, MajorMinorProjectActivity.class);
            startActivity(intent)
            finish()
        }, 2000)
    }

}