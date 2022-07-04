package com.example.pshiksha.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pshiksha.R;
import com.example.pshiksha.databinding.ServicesItemBinding;

import java.util.List;

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ServicesViewHolder> {

    private final Context context;
    private final List<Services> servicesList;
    private ServicesItemOnClickListener mOnClickListener = null;

    public ServicesRecyclerViewAdapter(Context context, List<Services> servicesList) {
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
        holder.textView.setText(servicesList.get(position).getTitle());
        holder.imageView.setImageResource(servicesList.get(position).getImageResId());
        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
        holder.itemView.setOnClickListener(v -> mOnClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        CardView cardView;

        public ServicesViewHolder(@NonNull ServicesItemBinding binding) {
            super(binding.getRoot());
            textView = binding.textView;
            imageView = binding.imageView;
            cardView = binding.cardView;
        }
    }

    public void setOnItemClickListener(ServicesItemOnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface ServicesItemOnClickListener {
        void onItemClick(int position);
    }
}
