package com.example.pshiksha.services.allServices

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.pshiksha.services.ServiceItem
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import com.example.pshiksha.databinding.ActivityMajorMinorProjectBinding
import com.example.pshiksha.services.ContactUsActivity
import com.example.pshiksha.utils.Util
import java.lang.StringBuilder

class MajorMinorProjectActivity : AppCompatActivity() {
    var binding: ActivityMajorMinorProjectBinding? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorMinorProjectBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        //
        val serviceItem = intent.getSerializableExtra("SERVICE") as ServiceItem?
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = serviceItem!!.title + " Service"
        binding!!.serviceImageView.setImageResource(serviceItem.imageResId)
        binding!!.serviceTitleTextView.text = serviceItem.title
        binding!!.serviceDescriptionTextView.text = serviceItem.description


        //
        binding!!.onCallClickButton.setOnClickListener {
            val databaseReference = firebaseDatabase!!.reference
                .child(Util.FIREBASE_USER_CONTACT_US)
                .child(firebaseAuth!!.currentUser!!.uid)
                .child(getValidName(serviceItem.title))
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var value = 0
                    if (snapshot.exists()) {
                        value = snapshot.getValue(Int::class.java)!!
                    }
                    databaseReference.setValue(value + 1)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:" + getString(R.string.pshiksha_phone_number))
//            startActivity(intent)
            startActivity(Intent(applicationContext, ContactUsActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getValidName(name: String): String {
        val builder = StringBuilder(name)
        for (i in builder.indices) {
            if (builder[i] == '/')
                builder[i] = ' '
        }
        return builder.toString()
    }
}