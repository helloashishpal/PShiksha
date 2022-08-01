package com.example.pshiksha.main.orders.order_history

import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.main.orders.OrderDetails
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pshiksha.R
import com.example.pshiksha.databinding.ActivityYourOrdersBinding
import com.example.pshiksha.main.orders.OrdersItemAdapter
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
import java.util.*

class OrderHistoryActivity : AppCompatActivity() {
    private var binding: ActivityYourOrdersBinding? = null
    private var adapter: OrdersItemAdapter? = null
    private val listOfOrders: MutableList<OrderDetails> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourOrdersBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        //
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        //
        initRecyclerView()
        getOrderFromDatabase(firebaseAuth, firebaseDatabase)
        binding!!.homeUpButton.setOnClickListener { onBackPressed() }
        binding!!.serviceStatusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.service_status_all_radio_btn -> {
                    //
                    adapter!!.setListOfOrders(listOfOrders)
                }
                R.id.service_status_pending_radio_btn -> {
                    //
                    val temp: MutableList<OrderDetails> = ArrayList()
                    for (i in listOfOrders.indices) {
                        if (listOfOrders[i].serviceStatus == OrderDetails.ServiceStatus.PENDING) {
                            temp.add(listOfOrders[i])
                        }
                    }
                    adapter!!.setListOfOrders(temp)
                }
                R.id.service_status_completed_radio_btn -> {
                    //
                    val temp: MutableList<OrderDetails> = ArrayList()
                    for (i in listOfOrders.indices) {
                        if (listOfOrders[i].serviceStatus == OrderDetails.ServiceStatus.COMPLETED) {
                            temp.add(listOfOrders[i])
                        }
                    }
                    adapter!!.setListOfOrders(temp)
                }
            }
        }
    }

    private fun getOrderFromDatabase(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ) {
        val loader = LoaderBuilder(this@OrderHistoryActivity)
        loader.setTitle("Fetching Orders...")
        loader.show()
        val currentUser = firebaseAuth.currentUser
        val databaseReference = firebaseDatabase.reference
        assert(currentUser != null)
        databaseReference.child(Util.FIREBASE_USERS)
            .child(currentUser!!.uid)
            .child(Util.FIREBASE_USER_ORDERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfOrders.clear()
                    for (dataSnapshot in snapshot.children) {
                        //order details
                        val orderDetails = dataSnapshot.getValue(
                            OrderDetails::class.java
                        )!!
                        listOfOrders.add(orderDetails)
                    }
                    listOfOrders.reverse()
                    adapter?.setListOfOrders(listOfOrders)
                    loader.hide()
                }

                override fun onCancelled(error: DatabaseError) {
                    loader.hide()
                }
            })
    }

    private fun initRecyclerView() {
        adapter = OrdersItemAdapter(applicationContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        binding!!.serviceStatusRadioGroup.check(R.id.service_status_all_radio_btn)
    }
}