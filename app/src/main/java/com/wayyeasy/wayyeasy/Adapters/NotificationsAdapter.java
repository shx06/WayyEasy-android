package com.wayyeasy.wayyeasy.Adapters;

import static com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers.BASE_URL;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.Models.OnlineCalls.call_response_model;
import com.wayyeasy.wayyeasy.R;

import java.util.Date;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.myViewHolder>{
    Context context;
    private List<call_response_model> data;
    private String token;
    public NotificationsAdapter(Context context, List<call_response_model> data, String token) {
        this.context = context;
        this.data = data;
        this.token = token;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification_item_design, parent, false);
        return new NotificationsAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.name.setText(data.get(position).getName());

        String imgUrl = BASE_URL + data.get(position).getImage();

        GlideUrl glideUrl = new GlideUrl(imgUrl,
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build());

        Glide.with(context).load(glideUrl).into(holder.image);


        holder.status.setText(data.get(position).getStatus());

        switch (data.get(position).getStatus()) {
            case Constants.CALL_MISSED:
                holder.status.setTextColor(context.getResources().getColor(R.color.red));
                holder.status.setText("Missed");
                break;
            case Constants.REJECT_CALL:
                holder.status.setTextColor(context.getResources().getColor(R.color.theme_color_orange));
                holder.status.setText("Cancelled");
                break;
            case Constants.RECEIVE_CALL:
                holder.status.setTextColor(context.getResources().getColor(R.color.green));
                holder.status.setText("Received");
                break;
        }

        try {

            SimpleDateFormat formatter = null;
            SimpleDateFormat formatterOut = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                formatterOut = new SimpleDateFormat("dd MMM yyyy HH:mm");

            }

            Date date = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                date = formatter.parse(data.get(position).getDate());
            }
            System.out.println(date);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.time.setText(String.valueOf(formatterOut.format(date)));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        holder.action.setOnClickListener(v-> {
            Toast.makeText(context, "initiate calling", Toast.LENGTH_SHORT).show();
        });

        holder.remove.setOnClickListener(v -> {
            data.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView name, time, status, action;
        ImageView image, remove;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.notification_name);
            image = itemView.findViewById(R.id.notification_img);
            time = itemView.findViewById(R.id.notification_time);
            status = itemView.findViewById(R.id.notification_status);
            action = itemView.findViewById(R.id.notification_action);
            remove = itemView.findViewById(R.id.notification_remove);
        }
    }
}
