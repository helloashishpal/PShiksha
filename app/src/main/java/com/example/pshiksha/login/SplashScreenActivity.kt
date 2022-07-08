package com.example.pshiksha.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.databinding.ActivitySplashScreenBinding
import com.example.pshiksha.services.ServicesActivity
import com.example.pshiksha.utils.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
                val currentUserUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                if (currentUserUid.isNotEmpty()) {
                    val rootRef = FirebaseDatabase.getInstance()
                        .getReference(Util.FIREBASE_USER_PROFILE_INFORMATION)
                        .child(currentUserUid)
                    rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                //Profile Exists.
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        ServicesActivity::class.java
                                    )
                                )
                            } else {
                                //Profile does not exists. open a ProfileSetupActivity
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        ProfileSetupActivity::class.java
                                    )
                                )
                            }
                            finishAffinity()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }

}