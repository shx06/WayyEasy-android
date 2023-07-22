package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.wayyeasy.wayyeasy.Activities.Hospitals.HospitalActivity;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Images;
import com.wayyeasy.wayyeasy.Models.Hospitals.search_response_model;
import com.wayyeasy.wayyeasy.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchHospital extends RecyclerView.Adapter<SearchHospital.myviewHolder> implements Filterable {

    public static final String TAG = "searchAdapter";
    List<search_response_model> data;
    ArrayList<search_response_model> backup;
    Context context;

    public SearchHospital(List<search_response_model> data, Context context) {
        this.data = data;
        this.context = context;
        backup = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_hospital_item, parent, false);
        return new myviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getAddress());

        String hospitalImage = data.get(position).getImages().get(0).split(",")[1];
        byte[] hospitalImageByte;
        Bitmap hospitalImageBitmap;

        hospitalImageByte = Base64.decode(hospitalImage, Base64.DEFAULT);
        hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);
        holder.image.setImageBitmap(hospitalImageBitmap);

        holder.itemLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, HospitalActivity.class);
            intent.putExtra("id", data.get(position).get_id());
            intent.putExtra("name", data.get(position).getName());
            intent.putExtra("address", data.get(position).getAddress());
            intent.putExtra("contact", data.get(position).getContactNumber());
            intent.putExtra("description", data.get(position).getDescription());
            intent.putExtra("pincode", data.get(position).getPincode());
            Images.setHospitalImages(data.get(position).getImages());
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
        public TextView name, address;
        CardView itemLayout;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            itemLayout = itemView.findViewById(R.id.single_user);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<search_response_model> filteredData = new ArrayList<>();
            if (keyword.toString().isEmpty())
                filteredData.addAll(backup);
            else {
                for (search_response_model obj : backup) {
                    if (obj.getName().toString().toLowerCase().contains(keyword.toString().toLowerCase(Locale.ROOT))) {
                        filteredData.add(obj);
                    } else if (obj.getAddress().toString().toLowerCase().contains(keyword.toString().toLowerCase())) {
                        filteredData.add(obj);
                    } else if (obj.getPincode().toString().toLowerCase().contains(keyword.toString().toLowerCase())) {
                        filteredData.add(obj);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((ArrayList<search_response_model>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
