package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.wayyeasy.wayyeasy.Activities.Hospitals.HospitalActivity;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.HospitalBooking;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Images;
import com.wayyeasy.wayyeasy.Models.Hospitals.hospital_response_model;
import com.wayyeasy.wayyeasy.R;
import java.util.List;

public class HospitalData extends RecyclerView.Adapter<HospitalData.holder> {

    private static final String TAG = "Close Data";
    List<hospital_response_model> data;
    Context context;

    public HospitalData(List<hospital_response_model> data, Context context) {
        this.data = data;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hospital_item_design, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        byte[] hospitalImageByte;
        Bitmap hospitalImageBitmap;

        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getAddress());

        String hospitalImage = data.get(position).getImages().get(0).split(",")[1];

        hospitalImageByte = Base64.decode(hospitalImage, Base64.DEFAULT);
        hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);
        holder.img.setImageBitmap(hospitalImageBitmap);

        holder.itemLayout.setOnClickListener(view -> {
            //Storing data from booking
            Images.setHospitalImages(data.get(position).getImages());
            HospitalBooking.setHospitalId(data.get(position).get_id());
            HospitalBooking.setHospitalOwner(data.get(position).getOwner());
            HospitalBooking.setHospitalName(data.get(position).getName());

            //Starting intent
            Intent intent = new Intent(context, HospitalActivity.class);
            intent.putExtra("id", data.get(position).get_id());
            intent.putExtra("name", data.get(position).getName());
            intent.putExtra("address", data.get(position).getAddress());
            intent.putExtra("contact", data.get(position).getContactNumber());
            intent.putExtra("description", data.get(position).getDescription());
            intent.putExtra("pincode", data.get(position).getPincode());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder {

        LinearLayoutCompat itemLayout;
        ImageView img;
        TextView name, address;
        RatingBar ratingBar;
        public holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.hospital_img);
            name = itemView.findViewById(R.id.hospital_name);
            address = itemView.findViewById(R.id.hospital_address);
            ratingBar = itemView.findViewById(R.id.hospital_ratings);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    public void add(hospital_response_model response) {
        data.add(response);
        notifyItemInserted(data.size() - 1);
    }
    public void addAll(List<hospital_response_model> postItems) {
        for (hospital_response_model response : postItems) {
            add(response);
        }
    }
}
