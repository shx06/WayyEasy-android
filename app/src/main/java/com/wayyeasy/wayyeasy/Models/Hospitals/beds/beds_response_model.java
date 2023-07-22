package com.wayyeasy.wayyeasy.Models.Hospitals.beds;

import android.os.Parcel;
import android.os.Parcelable;

public class beds_response_model implements Parcelable {
    private String hospital, owner, _id;
    private beds_response_model_sub rooms;

    public beds_response_model() {
    }

    public beds_response_model(String hospital, String owner, String _id, beds_response_model_sub rooms) {
        this.hospital = hospital;
        this.owner = owner;
        this._id = _id;
        this.rooms = rooms;
    }

    protected beds_response_model(Parcel in) {
        hospital = in.readString();
        owner = in.readString();
        _id = in.readString();
        rooms = in.readParcelable(beds_response_model_sub.class.getClassLoader());
    }

    public static final Creator<beds_response_model> CREATOR = new Creator<beds_response_model>() {
        @Override
        public beds_response_model createFromParcel(Parcel in) {
            return new beds_response_model(in);
        }

        @Override
        public beds_response_model[] newArray(int size) {
            return new beds_response_model[size];
        }
    };

    public String getHospital() {
        return hospital;
    }

    public String getOwner() {
        return owner;
    }

    public String get_id() {
        return _id;
    }

    public beds_response_model_sub getRooms() {
        return rooms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hospital);
        parcel.writeString(owner);
        parcel.writeString(_id);
        parcel.writeParcelable(rooms, i);
    }
}