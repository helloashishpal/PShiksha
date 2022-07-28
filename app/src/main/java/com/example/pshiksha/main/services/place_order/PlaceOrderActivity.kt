package com.example.pshiksha.main.services.place_order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pshiksha.databinding.ActivityPlaceOrderBinding
import com.example.pshiksha.main.services.ServiceItem
import com.example.pshiksha.main.orders.OrderDetails
import com.example.pshiksha.main.orders.TransactionDetails
import com.example.pshiksha.main.orders.order_history.OrderHistoryActivity
import com.example.pshiksha.utils.LoaderBuilder
import com.example.pshiksha.utils.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class PlaceOrderActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var binding: ActivityPlaceOrderBinding
    private var serviceItem: ServiceItem? = null
    private lateinit var loader: LoaderBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        supportActionBar?.hide()
        //
        serviceItem = intent.getSerializableExtra("SERVICE") as ServiceItem?
        //
        val btnMessage = "PAY ₹ ${serviceItem?.price}"
        binding.payButton.text = btnMessage
        binding.serviceNameTextView.text = serviceItem?.title
        binding.serviceImageView.setImageResource(serviceItem!!.imageResId)
        binding.serviceDescriptionEditText.hint = "Describe how you want your ${serviceItem?.title}"
        //
        binding.homeUpButton.setOnClickListener { onBackPressed() }
        binding.payButton.setOnClickListener {
            if (binding.serviceDescriptionEditText.text.toString().length <= 25) {
                Toast.makeText(
                    applicationContext,
                    "Description must be 25 characters long.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            startPayment()
        }
    }

    private fun startPayment() {
        loader = LoaderBuilder(this@PlaceOrderActivity).setTitle("Please wait...")
        loader.show()
        val activity: Activity = this@PlaceOrderActivity
        val checkout = Checkout()
        val currentUser = FirebaseAuth.getInstance().currentUser
        checkout.setKeyID(Util.RAZOR_PAY_API_KEY)
        try {
            val options = JSONObject()
            options.put("name", currentUser?.displayName)
            options.put("description", "Description Here.")
            options.put("image", Util.RAZOR_PAY_PROFILE_PHOTO)
            options.put("theme.color", Util.RAZOR_PAY_THEME_COLOR)
            options.put("currency", Util.RAZOR_PAY_CURRENCY)
//            options.put("order_id", mOrderId)
            options.put("amount", (serviceItem!!.price * 100).toString())
            val prefill = JSONObject()
            prefill.put("contact", currentUser?.phoneNumber)
            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: $e", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onPaymentSuccess(mTransactionID: String?, mPaymentData: PaymentData?) {
        loader.hide()
        Snackbar.make(
            binding.payButton as View,
            "Order Placed. Checkout more services",
            Snackbar.LENGTH_LONG
        )
            .setAction("View Orders") {
                startActivity(
                    Intent(
                        this@PlaceOrderActivity,
                        OrderHistoryActivity::class.java
                    )
                )
            }
            .show()
        if (mTransactionID != null) {
            pushOrderAndTransaction(mTransactionID)
        }
    }

    override fun onPaymentError(errorCode: Int, mTransactionID: String?, p2: PaymentData?) {
        when (errorCode) {
            Checkout.NETWORK_ERROR -> {
                Toast.makeText(applicationContext, "NETWORK_ERROR", Toast.LENGTH_SHORT).show()
            }
            Checkout.INVALID_OPTIONS -> {
                Toast.makeText(applicationContext, "INVALID_OPTIONS", Toast.LENGTH_SHORT).show()
            }
            Checkout.PAYMENT_CANCELED -> {
                Toast.makeText(applicationContext, "PAYMENT_CANCELED", Toast.LENGTH_SHORT).show()
            }
            Checkout.TLS_ERROR -> {
                Toast.makeText(applicationContext, "TLS_ERROR", Toast.LENGTH_SHORT).show()
            }
        }
        loader.hide()
    }

    private fun pushOrderAndTransaction(mTransactionID: String?) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val mUserUid = currentUser?.uid
        val mTransactionTime = System.currentTimeMillis().toString()
        val mOrderId = "order_ps" + System.currentTimeMillis().toString()
        val mPrice = serviceItem!!.price
        val transactionDetails =
            TransactionDetails(
                mTransactionID!!,
                mTransactionTime,
                mPrice
            )

        val orderDetails =
            OrderDetails(
                mUserUid!!,
                mOrderId,
                serviceItem!!.title,
                transactionDetails,
                binding.serviceDescriptionEditText.text.toString(),
                OrderDetails.ServiceStatus.PENDING
            )

        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference
            .child(Util.FIREBASE_USER_ORDERS)
            .child(mOrderId)
            .setValue(orderDetails)

        databaseReference
            .child(Util.FIREBASE_USERS)
            .child(mUserUid)
            .child(Util.FIREBASE_USER_ORDERS)
            .child(mOrderId)
            .setValue(orderDetails)
    }
}