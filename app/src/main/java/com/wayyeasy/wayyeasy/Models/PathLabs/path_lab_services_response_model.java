package com.wayyeasy.wayyeasy.Models.PathLabs;

import android.os.Parcel;
import android.os.Parcelable;

public class path_lab_services_response_model implements Parcelable {
    private String name, price, _id;

    public path_lab_services_response_model() {
    }

    public path_lab_services_response_model(String name, String price, String _id) {
        this.name = name;
        this.price = price;
        this._id = _id;
    }

    protected path_lab_services_response_model(Parcel in) {
        name = in.readString();
        price = in.readString();
        _id = in.readString();
    }

    public static final Creator<path_lab_services_response_model> CREATOR = new Creator<path_lab_services_response_model>() {
        @Override
        public path_lab_services_response_model createFromParcel(Parcel in) {
            return new path_lab_services_response_model(in);
        }

        @Override
        public path_lab_services_response_model[] newArray(int size) {
            return new path_lab_services_response_model[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
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
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(_id);
    }
}
