package com.example.pshiksha.services

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.pshiksha.services.YourOrdersRecyclerViewAdapter.YourOrdersRecyclerViewHolder
import com.example.pshiksha.transactions.OrderDetails
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import com.example.pshiksha.databinding.OrderItemBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class YourOrdersRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<YourOrdersRecyclerViewHolder>() {
    private var listOfOrders: List<OrderDetails> = ArrayList()
    fun setListOfOrders(listOfOrders: List<OrderDetails>) {
        this.listOfOrders = listOfOrders
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YourOrdersRecyclerViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context))
        return YourOrdersRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YourOrdersRecyclerViewHolder, position: Int) {
        val orderDetails = listOfOrders[position]
        val serviceTransaction = listOfOrders[position].serviceTransaction

        //
        holder.itemView.animation =
            AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        holder.binding.orderIdTextView.text = orderDetails.orderId
        //        holder.binding.orderDataDescriptionTextView = "Description Here."
        holder.binding.orderNameTextView.text = orderDetails.serviceName
        holder.binding.transactionIdTextView.text = serviceTransaction.transactionId
        holder.binding.transactionPriceTextView.text = "₹" + serviceTransaction.price
        val time = serviceTransaction.transactionTime
        val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
        val milliSeconds = time.toLong()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        //setting time
        holder.binding.orderDateTimeTextView.text = formatter.format(calendar.time)
    }

    override fun getItemCount(): Int {
        return listOfOrders.size
    }

    class YourOrdersRecyclerViewHolder(var binding: OrderItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}