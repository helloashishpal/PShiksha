package com.example.pshiksha.main.orders.all_orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pshiksha.R
import com.example.pshiksha.databinding.ActivityAllUserOrdersBinding
import com.example.pshiksha.main.orders.OrderDetails
import com.example.pshiksha.main.orders.OrderDetailsActivity
import com.example.pshiksha.main.orders.OrdersItemAdapter
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class AllUserOrdersActivity : AppCompatActivity() {
    private var binding: ActivityAllUserOrdersBinding? = null
    private var adapter: OrdersItemAdapter? = null
    private val listOfOrders: MutableList<OrderDetails> = ArrayList()
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserOrdersBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        //
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        //
        initRecyclerView()
        //
        getOrdersFromDatabase()
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

    private fun getOrdersFromDatabase() {
        val loader = LoaderBuilder(this@AllUserOrdersActivity)
        loader.setTitle("Fetching Orders...")
        loader.show()
        val databaseReference = firebaseDatabase.reference
        val currentUser = firebaseAuth.currentUser
        assert(currentUser != null)
        databaseReference.child(Util.FIREBASE_USER_ORDERS)
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
        adapter!!.setOnItemClickListener(object :
            OrdersItemAdapter.YourOrdersItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, OrderDetailsActivity::class.java)
                intent.putExtra("ORDER_DETAILS", listOfOrders[position])
                startActivity(intent)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        binding!!.serviceStatusRadioGroup.check(R.id.service_status_all_radio_btn)
        getOrdersFromDatabase()
    }
}