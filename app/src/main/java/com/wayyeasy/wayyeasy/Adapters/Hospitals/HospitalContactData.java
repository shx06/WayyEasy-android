package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wayyeasy.wayyeasy.R;

public class HospitalContactData extends RecyclerView.Adapter<HospitalContactData.holder> {

    private static final String TAG = "Hospital contact Data";
    String[] data;
    Context context;

    public HospitalContactData(String[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hospital_contact_number, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.contactNumber.setText(data[position]);
        holder.contact.setOnClickListener(view -> {
            Uri callUri = Uri.parse("tel:" + data[position]);
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(callUri);
            try {
                context.startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "emailSupport: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class holder extends RecyclerView.ViewHolder {
        TextView contactNumber;
        Button contact;
        public holder(@NonNull View itemView) {
            super(itemView);
            contactNumber = itemView.findViewById(R.id.contact_number);
            contact = itemView.findViewById(R.id.contact);
        }
    }
}
