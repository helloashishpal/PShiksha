package com.example.pshiksha.services

import androidx.appcompat.app.AppCompatActivity
import com.example.pshiksha.transactions.OrderDetails
import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pshiksha.databinding.ActivityYourOrdersBinding
import com.example.pshiksha.utils.Util
import java.util.*

class YourOrdersActivity : AppCompatActivity() {
    private var binding: ActivityYourOrdersBinding? = null
    private var adapter: YourOrdersRecyclerViewAdapter? = null
    private val listOfOrders: MutableList<OrderDetails> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourOrdersBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        //
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val databaseReference = firebaseDatabase.reference
        //
        initRecyclerView()
        assert(currentUser != null)
        databaseReference.child(Util.FIREBASE_USER_PROFILE_INFORMATION)
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
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        binding!!.homeUpButton.setOnClickListener { v: View? -> onBackPressed() }
    }

    private fun initRecyclerView() {
        adapter = YourOrdersRecyclerViewAdapter(applicationContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adapter!!.setListOfOrders(listOfOrders)
    }
}