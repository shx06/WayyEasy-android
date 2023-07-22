package com.wayyeasy.wayyeasy.Models.Hospitals.beds;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class beds_response_model_sub implements Parcelable {

    private String roomType, total, price;
    private ArrayList<String> image;
    private ArrayList<beds_response_model_sub_roomDetails> roomDetails;

    public beds_response_model_sub() {
    }

    public beds_response_model_sub(String roomType, String total, String price, ArrayList<String> image, ArrayList<beds_response_model_sub_roomDetails> roomDetails) {
        this.roomType = roomType;
        this.total = total;
        this.price = price;
        this.image = image;
        this.roomDetails = roomDetails;
    }

    protected beds_response_model_sub(Parcel in) {
        roomType = in.readString();
        total = in.readString();
        price = in.readString();
//        image = in.createStringArrayList();
        roomDetails = in.createTypedArrayList(beds_response_model_sub_roomDetails.CREATOR);
    }

    public static final Creator<beds_response_model_sub> CREATOR = new Creator<beds_response_model_sub>() {
        @Override
        public beds_response_model_sub createFromParcel(Parcel in) {
            return new beds_response_model_sub(in);
        }

        @Override
        public beds_response_model_sub[] newArray(int size) {
            return new beds_response_model_sub[size];
        }
    };

    public String getRoomType() {
        return roomType;
    }

    public String getTotal() {
        return total;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public ArrayList<beds_response_model_sub_roomDetails> getRoomDetails() {
        return roomDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(roomType);
        parcel.writeString(total);
        parcel.writeString(price);
//        parcel.writeStringList(image);
        parcel.writeTypedList(roomDetails);
    }
}
