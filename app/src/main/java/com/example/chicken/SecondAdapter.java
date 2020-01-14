package com.example.chicken;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class SecondAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {
    private ArrayList<Second_order_info> order_array;

    class Second_ViewHolder extends RecyclerView.ViewHolder{
        private TextView menu;
        private TextView location;
        private TextView pay;
        private TextView time;
        private TextView amount;
        public Second_ViewHolder(@NonNull View itemView){
            super(itemView);
            menu = itemView.findViewById(R.id.menu);
            location = itemView.findViewById(R.id.location);
            pay = itemView.findViewById(R.id.pay);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount);
        }
    }

    public SecondAdapter(ArrayList<Second_order_info> order_array){
        this.order_array = order_array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_item, parent, false);
        return new Second_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Second_ViewHolder viewHolder = (Second_ViewHolder) holder;
        viewHolder.menu.setText(order_array.get(position).getMenu());
        viewHolder.location.setText(order_array.get(position).getLocation());
        viewHolder.pay.setText(order_array.get(position).getPay());
        viewHolder.time.setText(order_array.get(position).getTime());
        viewHolder.amount.setText(order_array.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return order_array.size();
    }
}
