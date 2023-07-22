package com.wayyeasy.wayyeasy.Models.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import java.util.ArrayList;

public class fetched_physician_list_response_mode implements Parcelable {
    private String message;
    private ArrayList<physician_response_model> data;

    public fetched_physician_list_response_mode() {
    }

    public fetched_physician_list_response_mode(String message, ArrayList<physician_response_model> data) {
        this.message = message;
        this.data = data;
    }

    protected fetched_physician_list_response_mode(Parcel in) {
        message = in.readString();
        data = in.createTypedArrayList(physician_response_model.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<fetched_physician_list_response_mode> CREATOR = new Creator<fetched_physician_list_response_mode>() {
        @Override
        public fetched_physician_list_response_mode createFromParcel(Parcel in) {
            return new fetched_physician_list_response_mode(in);
        }

        @Override
        public fetched_physician_list_response_mode[] newArray(int size) {
            return new fetched_physician_list_response_mode[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public ArrayList<physician_response_model> getData() {
        return data;
    }
}
