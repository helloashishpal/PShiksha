package com.example.pshiksha.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pshiksha.R
import com.example.pshiksha.databinding.ActivityMainBinding
import com.example.pshiksha.login.LoginActivity
import com.example.pshiksha.login.UserInformation
import com.example.pshiksha.main.about_us.AboutUsActivity
import com.example.pshiksha.main.contact_us.ContactUsActivity
import com.example.pshiksha.main.orders.all_orders.AllUserOrdersActivity
import com.example.pshiksha.main.orders.order_history.OrderHistoryActivity
import com.example.pshiksha.main.profile_edit.EditUserProfileActivity
import com.example.pshiksha.main.services.ServiceItem
import com.example.pshiksha.main.services.ServicesItemAdapter
import com.example.pshiksha.main.services.ServiceDetailsActivity
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
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

        if (firebaseAuth.currentUser == null) return
        getCurrentUserDetails()

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_ed_sheet_making_title),
                getString(R.string.service_ed_sheet_making_description),
                R.drawable.ic_service_ed_sheet_making,
                ServiceDetailsActivity::class.java,
                600
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_assignment_file_ppt_report_title),
                getString(R.string.service_assignment_file_ppt_report_description),
                R.drawable.ic_service_assignment_file_ppt_report,
                ServiceDetailsActivity::class.java,
                600
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_professional_cv_making_title),
                getString(R.string.service_professional_cv_making_description),
                R.drawable.ic_service_professional_cv_making,
                ServiceDetailsActivity::class.java,
                600
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_placement_preparation_title),
                getString(R.string.service_placement_preparation_description),
                R.drawable.ic_service_placement_preparation,
                ServiceDetailsActivity::class.java,
                600
            )
        )

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_major_minor_project_title),
                getString(R.string.service_major_minor_project_description),
                R.drawable.ic_service_major_minor_project,
                ServiceDetailsActivity::class.java,
                600
            )
        )

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_plagiarism_removal_thesis_guidance_title),
                getString(R.string.service_plagiarism_removal_thesis_guidance_description),
                R.drawable.ic_service_plagiarism_removal_thesis_guidance,
                ServiceDetailsActivity::class.java,
                600
            )
        )

        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_coaching_immigration_title),
                getString(R.string.service_coaching_immigration_description),
                R.drawable.ic_service_coaching_immigration,
                ServiceDetailsActivity::class.java,
                600
            )
        )
        serviceItemList.add(
            ServiceItem(
                getString(R.string.service_tech_non_tech_internship_title),
                getString(R.string.service_tech_non_tech_internship_description),
                R.drawable.ic_service_tech_non_tech_internship,
                ServiceDetailsActivity::class.java,
                600
            )
        )
        initRecyclerView()


        binding.profileImageButton.setOnClickListener {
            intent = Intent(applicationContext, EditUserProfileActivity::class.java)
            startActivity(intent.putExtra("USER_INFORMATION", userInformation))
        }
    }

    private fun getCurrentUserDetails() {
        val loader = LoaderBuilder(this)
            .setTitle("Loading Profile...")
        loader.show()

        firebaseDatabase.getReference(Util.FIREBASE_USER_PROFILE_INFORMATION)
            .child(firebaseAuth.currentUser!!.uid)
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