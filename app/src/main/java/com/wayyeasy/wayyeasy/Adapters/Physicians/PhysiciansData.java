package com.wayyeasy.wayyeasy.Adapters.Physicians;

import static com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers.BASE_URL;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wayyeasy.wayyeasy.Activities.Physicians.PhysicianDetails;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.R;
import java.util.List;

public class PhysiciansData extends RecyclerView.Adapter<PhysiciansData.myviewHolder>{

    List<physician_response_model> data;
    Context context;
    private String token;

    public PhysiciansData(List<physician_response_model> data, Context context, String token) {
        this.data = data;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_physician_item_design, parent, false);
        return new PhysiciansData.myviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.desc.setText(data.get(position).getDescription());
        holder.speciality.setText(data.get(position).getSpecialityType());
        holder.qualification.setText(data.get(position).getQualification());
        holder.name.setText(data.get(position).getName());

        String imgUrl = BASE_URL + data.get(position).getImage();

        GlideUrl glideUrl = new GlideUrl(imgUrl,
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build());

        Glide.with(context).load(glideUrl).into(holder.image);

        holder.itemLayout.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PhysicianDetails.class);
            intent.putExtra(Constants.physicianData, data.get(position));
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
        public TextView name, desc, speciality, qualification;
        CardView itemLayout;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.doc_speciality);
            qualification = itemView.findViewById(R.id.doc_qualification);
            desc = itemView.findViewById(R.id.description);
            itemLayout = itemView.findViewById(R.id.single_user);
        }
    }
}
