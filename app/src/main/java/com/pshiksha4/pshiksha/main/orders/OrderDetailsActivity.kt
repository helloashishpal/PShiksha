package com.pshiksha4.pshiksha.main.orders

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pshiksha4.pshiksha.R
import com.pshiksha4.pshiksha.databinding.ActivityOrderDetailsBinding
import com.pshiksha4.pshiksha.login.UserInformation
import com.pshiksha4.pshiksha.utils.LoaderBuilder
import com.pshiksha4.pshiksha.utils.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var loader: LoaderBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOrderDetailsBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        //
        supportActionBar!!.hide()
        loader = LoaderBuilder(this)
        loader.setTitle("Fetching Information...")
        loader.show()

        //
        val orderDetails = intent.getSerializableExtra("ORDER_DETAILS") as OrderDetails?
        binding.orderIdTextView.text = orderDetails!!.orderId
        binding.serviceNameTextView.text = orderDetails.serviceName
        binding.transactionIdTextView.text = orderDetails.serviceTransaction.transactionId
        val priceText = "₹" + orderDetails.serviceTransaction.price.toString()
        binding.transactionPriceTextView.text = priceText
        binding.userDescriptionTextView.text = orderDetails.serviceDescription
        binding.serviceStatusTextView.text = orderDetails.serviceStatus.name
        if (orderDetails.serviceStatus == OrderDetails.ServiceStatus.PENDING) {
            binding.serviceStatusTextView.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.yellow_dark
                )
            )
            binding.markAsPendingButton.visibility = View.GONE
        } else {
            binding.serviceStatusTextView.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.green_dark
                )
            )
            binding.markAsCompletedButton.visibility = View.GONE
        }


        //setting time
        val time = orderDetails.serviceTransaction.transactionTime
        val formatter: DateFormat = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
        val milliSeconds = time.toLong()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        binding.transactionTimeTextView.text = formatter.format(calendar.time).toString()
        //setting time
        //
        binding.homeUpButton.setOnClickListener { onBackPressed() }
        binding.markAsPendingButton.setOnClickListener {
            changeServiceStatus(
                orderDetails,
                OrderDetails.ServiceStatus.PENDING
            )
        }
        binding.markAsCompletedButton.setOnClickListener {
            changeServiceStatus(
                orderDetails,
                OrderDetails.ServiceStatus.COMPLETED
            )
        }

        //

        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference
            .child(Util.FIREBASE_USERS)
            .child(orderDetails.userUid)
            .child(Util.FIREBASE_USER_INFORMATION)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInformation = snapshot.getValue(UserInformation::class.java)
                    binding.userNameTextView.text = userInformation?.fullName
                    binding.userPhoneNumberTextView.text = userInformation?.phoneNumber
                    val txt =
                        "${userInformation?.collegeName}, ${userInformation?.collegeDegree}, ${userInformation?.collegeBranch}, ${userInformation?.collegeGraduationYear}"
                    binding.userCollegeNameTextView.text = txt

                    binding.callUserButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel: ${userInformation?.phoneNumber}")
                        startActivity(intent)
                    }
                    loader.hide()
                }

                override fun onCancelled(error: DatabaseError) {
                    loader.hide()
                }
            })
    }

    private fun changeServiceStatus(
        orderDetails: OrderDetails,
        mServiceStatus: OrderDetails.ServiceStatus
    ) {
        orderDetails.serviceStatus = mServiceStatus
        val loader = LoaderBuilder(this@OrderDetailsActivity)
        loader.setTitle("Updating Order Status...")
        loader.show()

        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference
            .child(Util.FIREBASE_USERS)
            .child(orderDetails.userUid)
            .child(Util.FIREBASE_USER_ORDERS)
            .child(orderDetails.orderId)
            .setValue(orderDetails)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    databaseReference
                        .child(Util.FIREBASE_USER_ORDERS)
                        .child(orderDetails.orderId)
                        .setValue(orderDetails)
                        .addOnCompleteListener { result2 ->
                            if (result2.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Order Updated Successfully",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                finish()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Something went wrong..",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                loader.hide()
                            }

                        }

                } else {
                    Toast.makeText(applicationContext, "Something went wrong..", Toast.LENGTH_LONG)
                        .show()
                    loader.hide()
                }
            }
    }
}