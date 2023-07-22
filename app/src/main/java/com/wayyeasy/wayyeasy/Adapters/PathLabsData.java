package com.wayyeasy.wayyeasy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.wayyeasy.wayyeasy.Activities.PathLabs.PathLabActivity;
import com.wayyeasy.wayyeasy.Models.PathLabs.pathLabs.path_lab_response_model;
import com.wayyeasy.wayyeasy.R;
import java.util.List;

public class PathLabsData extends RecyclerView.Adapter<PathLabsData.myviewHolder> {

    List<path_lab_response_model> data;
    Context context;

    public PathLabsData(List<path_lab_response_model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_path_labs_design, parent, false);
        return new PathLabsData.myviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.desc.setText(data.get(position).getDescription());
        holder.itemLayout.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PathLabActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myviewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public TextView name, desc;
        CardView itemLayout;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.path_labs_image);
            name = itemView.findViewById(R.id.path_labs_name);
            desc = itemView.findViewById(R.id.path_labs_desc);
            itemLayout = itemView.findViewById(R.id.path_lab_single_layout);
        }
    }
}
