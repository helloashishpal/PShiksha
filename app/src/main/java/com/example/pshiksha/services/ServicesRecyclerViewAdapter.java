package com.example.pshiksha.services;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pshiksha.databinding.ServicesItemBinding;

import java.util.List;

public class ServicesRecyclerView extends RecyclerView.Adapter<ServicesRecyclerView.ServicesViewHolder> {

    private final Context context;
    private final List<Services> servicesList;
    private ServicesItemOnClickListener mOnClickListener = null;

    public ServicesRecyclerView(Context context, List<Services> servicesList) {
        this.context = context;
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServicesItemBinding binding = ServicesItemBinding.inflate(LayoutInflater.from(context));
        return new ServicesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        holder.textView.setText(servicesList.get(position).getTitle());
        holder.imageView.setImageResource(servicesList.get(position).getImageResId());
        holder.imageView.setTranslationX(width / 6.0f);
        holder.itemView.setOnClickListener(v -> mOnClickListener.onItemClick(position));
//            holder.imageView  = servicesList.get(position).getColor();
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ServicesViewHolder(@NonNull ServicesItemBinding binding) {
            super(binding.getRoot());
            textView = binding.textView;
            imageView = binding.imageView;
        }
    }

    public void setOnItemClickListener(ServicesItemOnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface ServicesItemOnClickListener {
        void onItemClick(int position);
    }
}
