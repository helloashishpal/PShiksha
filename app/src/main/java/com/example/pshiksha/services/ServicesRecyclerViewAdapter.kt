package com.example.pshiksha.services

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.example.pshiksha.services.ServicesRecyclerViewAdapter.ServicesViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.pshiksha.databinding.ServicesItemBinding

class ServicesRecyclerViewAdapter(
    private val context: Context,
    private val serviceItemList: List<ServiceItem>
) : RecyclerView.Adapter<ServicesViewHolder>() {
    private var mOnClickListener: ServicesItemOnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val binding = ServicesItemBinding.inflate(
            LayoutInflater.from(
                context
            )
        )
        return ServicesViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.textView.text = serviceItemList[position].title
        holder.imageView.setImageResource(serviceItemList[position].imageResId)
        holder.itemView.setOnClickListener { mOnClickListener!!.onItemClick(position) }
    }

    override fun getItemCount(): Int {
        return serviceItemList.size
    }

    class ServicesViewHolder(binding: ServicesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var textView: TextView
        var imageView: ImageView
        var cardView: CardView

        init {
            textView = binding.textView
            imageView = binding.imageView
            cardView = binding.cardView
        }
    }

    fun setOnItemClickListener(onClickListener: ServicesItemOnClickListener) {
        mOnClickListener = onClickListener
    }

    interface ServicesItemOnClickListener {
        fun onItemClick(position: Int)
    }
}