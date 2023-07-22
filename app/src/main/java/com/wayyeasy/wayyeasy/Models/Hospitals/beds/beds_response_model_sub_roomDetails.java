package com.wayyeasy.wayyeasy.Models.Hospitals.beds;

import android.os.Parcel;
import android.os.Parcelable;

public class beds_response_model_sub_roomDetails implements Parcelable {
    private String roomNo, available, _id;

    public beds_response_model_sub_roomDetails() {
    }

    public beds_response_model_sub_roomDetails(String roomNo, String available, String _id) {
        this.roomNo = roomNo;
        this.available = available;
        this._id = _id;
    }

    protected beds_response_model_sub_roomDetails(Parcel in) {
        roomNo = in.readString();
        available = in.readString();
        _id = in.readString();
    }

    public static final Creator<beds_response_model_sub_roomDetails> CREATOR = new Creator<beds_response_model_sub_roomDetails>() {
        @Override
        public beds_response_model_sub_roomDetails createFromParcel(Parcel in) {
            return new beds_response_model_sub_roomDetails(in);
        }

        @Override
        public beds_response_model_sub_roomDetails[] newArray(int size) {
            return new beds_response_model_sub_roomDetails[size];
        }
    };

    public String getRoomNo() {
        return roomNo;
    }

    public String getAvailable() {
        return available;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(roomNo);
        parcel.writeString(available);
        parcel.writeString(_id);
    }
}
