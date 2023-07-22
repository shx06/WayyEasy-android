package com.wayyeasy.wayyeasy.Activities.Search.Physicians;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wayyeasy.wayyeasy.R;

import java.util.List;

public class PhysiciansTypeAdapter extends RecyclerView.Adapter<PhysiciansTypeAdapter.myViewHolder> {

    List<physicians_type_response_model> data;

    private OnItemClickListener listener;

    interface OnItemClickListener {
        void onItemClick(physicians_type_response_model item);
    }

    public PhysiciansTypeAdapter(List<physicians_type_response_model> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhysiciansTypeAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.physician_type_single_item, parent, false);
        return new PhysiciansTypeAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhysiciansTypeAdapter.myViewHolder holder, int position) {

        holder.type.setText(data.get(position).getType());

        holder.item.setOnClickListener(view -> {
            listener.onItemClick(data.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        LinearLayout item;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            item = itemView.findViewById(R.id.single_layout);
        }
    }
}