package com.example.pshiksha.services

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pshiksha.R
import com.example.pshiksha.databinding.ActivityServicesBinding
import com.example.pshiksha.login.UserInformation
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ServicesActivity : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityServicesBinding
    private lateinit var userInformation: UserInformation
    private val servicesList: MutableList<Services> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name) + " Services"

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) return


                override fun onCancelled(error: DatabaseError) {
                }
            })
        servicesList.add(Services("", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE1", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE2", R.drawable.ic_college_24, R.color.yellow, null))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE3", R.drawable.ic_person_24, R.color.yellow, null))
        servicesList.add(Services("TITLE4", R.drawable.ic_person_24, R.color.yellow, null))

        initRecyclerView()


        binding.profileImageButton.setOnClickListener {
            intent = Intent(applicationContext, EditUserProfileActivity::class.java)
            startActivity(intent.putExtra("USER_INFORMATION", userInformation))
        }
    }

    private fun initRecyclerView() {
        val adapter = ServicesRecyclerViewAdapter(
            this,
            servicesList
        )
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { position: Int ->
            Toast.makeText(
                this,
                "POSITION : $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val loader = LoaderBuilder(this)
            .setTitle("Loading Profile...")
        loader.show()

        firebaseDatabase.getReference(Util.FIREBASE_USER_PROFILE_INFORMATION)
            .child(firebaseAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInformation = snapshot.getValue(UserInformation::class.java)
                    if (userInformation != null) {
                        this@ServicesActivity.userInformation = userInformation
                    }
                    val title = "Hello, ${userInformation?.fullName}"
                    binding.textView.text = title
                    loader.hide()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}