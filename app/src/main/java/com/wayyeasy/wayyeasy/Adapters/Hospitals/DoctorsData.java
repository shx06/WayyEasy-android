package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wayyeasy.wayyeasy.Models.Hospitals.doctors_response_model;
import com.wayyeasy.wayyeasy.R;

import java.util.List;

public class DoctorsData extends RecyclerView.Adapter<DoctorsData.holder> {

    private static final String TAG = "Doctors Data";
    List<doctors_response_model> data;
    Context context;

    public DoctorsData(List<doctors_response_model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hospital_doctor_details, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        String count = String.valueOf(position + 1);
        holder.slNo.setText(count);
        holder.age.setText(data.get(position).getAge());
        holder.name.setText(data.get(position).getName());
        holder.qualification.setText(data.get(position).getQualification());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder {

        TextView slNo, name, qualification, age;
        public holder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.sl_no);
            age = itemView.findViewById(R.id.age);
            name = itemView.findViewById(R.id.doctor_name);
            qualification = itemView.findViewById(R.id.doctor_qualification);
        }
    }
}
