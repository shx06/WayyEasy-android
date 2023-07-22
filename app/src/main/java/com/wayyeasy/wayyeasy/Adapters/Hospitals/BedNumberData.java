package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wayyeasy.wayyeasy.ComponentFiles.Constants.HospitalBooking;
import com.wayyeasy.wayyeasy.ComponentFiles.SocketHandler;
import com.wayyeasy.wayyeasy.Models.Hospitals.beds.beds_response_model_sub_roomDetails;
import com.wayyeasy.wayyeasy.Models.Hospitals.bookings.room_book_request_user_to_owner;
import com.wayyeasy.wayyeasy.R;

import java.util.ArrayList;
import java.util.List;

public class BedNumberData extends RecyclerView.Adapter<BedNumberData.holder> {

    private static final String TAG = "Beds Data";
    List<beds_response_model_sub_roomDetails> data;
    Context context;
    private String userId, userName;
    Dialog dialog;

    public BedNumberData(List<beds_response_model_sub_roomDetails> data, Context context, String userId, String userName) {
        this.data = data;
        this.context = context;
        this.userId = userId;
        this.userName = userName;
    }

    @NonNull
    @Override
    public BedNumberData.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hospital_bed_no_details, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BedNumberData.holder holder, int position) {
        holder.roomNo.setText(data.get(position).getRoomNo());
        if (data.get(position).getAvailable().equals("yes"))
            holder.room.setBackground(context.getResources().getDrawable(R.drawable.rooms_available));
        else
            holder.room.setBackground(context.getResources().getDrawable(R.drawable.rooms_unavailable));

        holder.room.setOnClickListener(view -> {
            if (!data.get(position).getAvailable().equals("yes")) {
                Toast.makeText(context, "Selected room not available!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            HospitalBooking.setRoomId(data.get(position).get_id());
            HospitalBooking.setRoomNo(data.get(position).getRoomNo());

            confirmBooking(view);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder {

        TextView roomNo, room;

        public holder(@NonNull View itemView) {
            super(itemView);
            roomNo = itemView.findViewById(R.id.room_no);
            room = itemView.findViewById(R.id.room);
        }
    }

    private void confirmBooking(View v) {

        dialog = new Dialog(v.getRootView().getContext());
        TextView hospitalName, roomType, roomNumber;
        Button confirmBooking;
        dialog.setContentView(R.layout.booking_details_dialog);
        dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.background_rounded_white_round));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        hospitalName = dialog.findViewById(R.id.booked_hospital_name);
        roomType = dialog.findViewById(R.id.booked_bed_type);
        roomNumber = dialog.findViewById(R.id.booked_room_number);
        confirmBooking = dialog.findViewById(R.id.confirm_booking);

        hospitalName.setText(HospitalBooking.getHospitalName());
        roomType.setText(HospitalBooking.getRoomType());
        roomNumber.setText(HospitalBooking.getRoomNo());

        confirmBooking.setOnClickListener(view -> {
            List<room_book_request_user_to_owner> bookingData = new ArrayList<room_book_request_user_to_owner>();
            bookingData.add(new room_book_request_user_to_owner(HospitalBooking.getHospitalId(), HospitalBooking.getRoomId(), HospitalBooking.getRoomNo(), HospitalBooking.getRoomType(), HospitalBooking.getHospitalOwner(), userId, userName));
            try {
                //Socket connection
                SocketHandler socketHandler = new SocketHandler(bookingData, context);
                socketHandler.run();
                dialog.dismiss();
            } catch (Exception e) {
                Log.d(TAG, "confirmBooking: " + e.getMessage());
            }
        });
        dialog.show();
    }
}
