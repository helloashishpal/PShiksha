package com.pshiksha4.pshiksha.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pshiksha4.pshiksha.R
import com.pshiksha4.pshiksha.databinding.ActivityMainBinding
import com.pshiksha4.pshiksha.login.LoginActivity
import com.pshiksha4.pshiksha.login.ProfileSetupActivity
import com.pshiksha4.pshiksha.login.UserInformation
import com.pshiksha4.pshiksha.main.about_us.AboutUsActivity
import com.pshiksha4.pshiksha.main.contact_us.ContactUsActivity
import com.pshiksha4.pshiksha.main.orders.all_orders.AllUserOrdersActivity
import com.pshiksha4.pshiksha.main.orders.order_history.OrderHistoryActivity
import com.pshiksha4.pshiksha.main.profile_edit.EditUserProfileActivity
import com.pshiksha4.pshiksha.main.services.ServiceItem
import com.pshiksha4.pshiksha.main.services.ServicesItemAdapter
import com.pshiksha4.pshiksha.main.services.ServiceDetailsActivity
import com.pshiksha4.pshiksha.utils.LoaderBuilder
import com.pshiksha4.pshiksha.utils.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private var userInformation: UserInformation? = null
    private val serviceItemList: MutableList<ServiceItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        isCurrentUserRegistered()

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_ed_sheet_making_title),
                getString(R.string.service_ed_sheet_making_description),
                R.drawable.ic_service_ed_sheet_making,
                ServiceDetailsActivity::class.java,
                49
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_assignment_file_ppt_report_title),
                getString(R.string.service_assignment_file_ppt_report_description),
                R.drawable.ic_service_assignment_file_ppt_report,
                ServiceDetailsActivity::class.java,
                49
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_professional_cv_making_title),
                getString(R.string.service_professional_cv_making_description),
                R.drawable.ic_service_professional_cv_making,
                ServiceDetailsActivity::class.java,
                49
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_placement_preparation_title),
                getString(R.string.service_placement_preparation_description),
                R.drawable.ic_service_placement_preparation,
                ServiceDetailsActivity::class.java,
                49
            )
        )

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_major_minor_project_title),
                getString(R.string.service_major_minor_project_description),
                R.drawable.ic_service_major_minor_project,
                ServiceDetailsActivity::class.java,
                49
            )
        )

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_plagiarism_removal_thesis_guidance_title),
                getString(R.string.service_plagiarism_removal_thesis_guidance_description),
                R.drawable.ic_service_plagiarism_removal_thesis_guidance,
                ServiceDetailsActivity::class.java,
                49
            )
        )

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_coaching_immigration_title),
                getString(R.string.service_coaching_immigration_description),
                R.drawable.ic_service_coaching_immigration,
                ServiceDetailsActivity::class.java,
                49
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_tech_non_tech_internship_title),
                getString(R.string.service_tech_non_tech_internship_description),
                R.drawable.ic_service_tech_non_tech_internship,
                ServiceDetailsActivity::class.java,
                49
            )
        )
        initRecyclerView()


        binding.profileImageButton.setOnClickListener {
            intent = Intent(applicationContext, EditUserProfileActivity::class.java)
            startActivity(intent.putExtra("USER_INFORMATION", userInformation))
        }
    }

    private fun isCurrentUserRegistered() {
        val loader = LoaderBuilder(this)
            .setTitle("Checking Profile...")
        loader.show()
        val currentUserUid: String = firebaseAuth.currentUser!!.uid
        val reference = firebaseDatabase.reference

        reference
            .child(Util.FIREBASE_USERS)
            .child(currentUserUid)
            .child(Util.FIREBASE_USER_INFORMATION)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        //Profile does not exists. open a ProfileSetupActivity
                        startActivity(
                            Intent(
                                applicationContext,
                                ProfileSetupActivity::class.java
                            )
                        )
                        finishAffinity()
                    } else {
                        getCurrentUserDetails()
                    }
                    loader.hide()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun getCurrentUserDetails() {
        val loader = LoaderBuilder(this)
            .setTitle("Loading Profile...")
        loader.show()

        firebaseDatabase
            .getReference(Util.FIREBASE_USERS)
            .child(firebaseAuth.currentUser!!.uid)
            .child(Util.FIREBASE_USER_INFORMATION)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInformation = snapshot.getValue(UserInformation::class.java)
                    this@MainActivity.userInformation = userInformation
                    val title = "Hello, ${userInformation!!.fullName}"
                    binding.textView.text = title
                    invalidateOptionsMenu()
                    loader.hide()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun initRecyclerView() {
        val adapter = ServicesItemAdapter(
            this,
            serviceItemList
        )
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :
            ServicesItemAdapter.ServicesItemOnClickListener {
            override fun onItemClick(position: Int) {
                val nextActivity = serviceItemList[position].onClickActivity
                val intent = Intent(applicationContext, nextActivity)
                intent.putExtra("SERVICE", serviceItemList[position])
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.services_menu, menu)
        val item = menu!!.findItem(R.id.menu_services_all_user_orders)
        item.isVisible = false
        if (userInformation != null) {
            if (userInformation?.admin == true)
                item.isVisible = true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_services_contact_us -> {
                startActivity(Intent(applicationContext, ContactUsActivity::class.java))
            }
            R.id.menu_services_about_us -> {
                startActivity(Intent(applicationContext, AboutUsActivity::class.java))
            }
            R.id.menu_services_log_out -> {
                firebaseAuth.signOut()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finishAffinity()
            }
            R.id.menu_services_your_orders -> {
                startActivity(Intent(applicationContext, OrderHistoryActivity::class.java))
            }
            R.id.menu_services_all_user_orders -> {
                startActivity(Intent(applicationContext, AllUserOrdersActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}