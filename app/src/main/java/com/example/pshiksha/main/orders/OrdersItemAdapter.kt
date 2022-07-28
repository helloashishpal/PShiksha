package com.example.pshiksha.main.orders

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.example.pshiksha.main.orders.OrdersItemAdapter.OrdersItemViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.example.pshiksha.R
import com.example.pshiksha.databinding.OrderItemBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrdersItemAdapter(private val context: Context) :
    RecyclerView.Adapter<OrdersItemViewHolder>() {
    private var listOfOrders: List<OrderDetails> = ArrayList()
    private var yourOrdersItemClickListener: YourOrdersItemClickListener? = null

    //
    fun setListOfOrders(listOfOrders: List<OrderDetails>) {
        this.listOfOrders = listOfOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersItemViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context))
        return OrdersItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: OrdersItemViewHolder, position: Int) {
        val orderDetails = listOfOrders[position]
        val serviceTransaction = listOfOrders[position].serviceTransaction

        //
        holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        holder.binding.orderIdTextView.text = orderDetails.orderId
        //        holder.binding.orderDataDescriptionTextView = "Description Here."
        holder.binding.orderNameTextView.text = orderDetails.serviceName
        holder.binding.transactionIdTextView.text = serviceTransaction.transactionId
        holder.binding.transactionPriceTextView.text = "₹" + serviceTransaction.price
        holder.binding.orderDataDescriptionTextView.text = orderDetails.serviceDescription
        holder.binding.serviceStatusTextView.text = orderDetails.serviceStatus.name
        if (orderDetails.serviceStatus == OrderDetails.ServiceStatus.PENDING) {
            holder.binding.serviceStatusTextView.setTextColor(context.getColor(R.color.yellow_dark))
        } else {
            holder.binding.serviceStatusTextView.setTextColor(context.getColor(R.color.green_dark))
        }
        //setting time
        val time = serviceTransaction.transactionTime
        val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
        val milliSeconds = time.toLong()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        holder.binding.orderDateTimeTextView.text = formatter.format(calendar.time)

        //adding on click
        holder.itemView.setOnClickListener {
            if (this.yourOrdersItemClickListener != null) {
                yourOrdersItemClickListener!!.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfOrders.size
    }

    class OrdersItemViewHolder(var binding: OrderItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

    fun setOnItemClickListener(mListener: YourOrdersItemClickListener) {
        this.yourOrdersItemClickListener = mListener
    }

    interface YourOrdersItemClickListener {
        fun onItemClick(position: Int)
    }
}