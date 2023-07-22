package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wayyeasy.wayyeasy.Activities.Hospitals.RoomDetails;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Images;
import com.wayyeasy.wayyeasy.Models.Hospitals.beds.beds_response_model;
import com.wayyeasy.wayyeasy.R;

import java.util.List;

public class BedsData extends RecyclerView.Adapter<BedsData.holder> {

    private static final String TAG = "Beds Data";
    List<beds_response_model> data;
    Context context;

    public BedsData(List<beds_response_model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hospital_bed_details, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        String count = String.valueOf(position + 1);
        holder.slNo.setText(count);
        holder.bedType.setText(data.get(position).getRooms().getRoomType());
        holder.total.setText(data.get(position).getRooms().getTotal());
        holder.price.setText(data.get(position).getRooms().getPrice());

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Images.setRoomImages(data.get(position).getRooms().getImage());
                Intent intent = new Intent(context, RoomDetails.class);
                intent.putExtra("bedDetails", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return data.size();
        } catch (Exception e){
            Log.d(TAG, "getItemCount: " + e.toString());
            return 0;
        }
    }

    class holder extends RecyclerView.ViewHolder {

        TextView slNo, bedType, total, book, price;
        RecyclerView roomDetailsRV;
        public holder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.sl_no);
            bedType = itemView.findViewById(R.id.bed_type);
            total = itemView.findViewById(R.id.total_beds);
            book = itemView.findViewById(R.id.book);
            price = itemView.findViewById(R.id.price);
            roomDetailsRV = itemView.findViewById(R.id.room_details_rv);
        }
    }
}
