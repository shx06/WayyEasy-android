package com.wayyeasy.wayyeasy.Models.PathLabs.pathLabs;

import android.os.Parcel;
import android.os.Parcelable;

import com.wayyeasy.wayyeasy.Models.PathLabs.path_lab_services_response_model;

import java.util.ArrayList;

public class path_lab_response_model implements Parcelable {
    private String name,
            description, city;
    private ArrayList<String> images;
    private ArrayList<path_lab_ratings_response_model> userRatings;

    public path_lab_response_model() {
    }

    public path_lab_response_model(String name, String description, String city, ArrayList<String> images, ArrayList<path_lab_ratings_response_model> userRatings) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.images = images;
        this.userRatings = userRatings;
    }

    protected path_lab_response_model(Parcel in) {
        name = in.readString();
        description = in.readString();
        city = in.readString();
        images = in.createStringArrayList();
        userRatings = in.createTypedArrayList(path_lab_ratings_response_model.CREATOR);
    }

    public static final Creator<path_lab_response_model> CREATOR = new Creator<path_lab_response_model>() {
        @Override
        public path_lab_response_model createFromParcel(Parcel in) {
            return new path_lab_response_model(in);
        }

        @Override
        public path_lab_response_model[] newArray(int size) {
            return new path_lab_response_model[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<path_lab_ratings_response_model> getUserRatings() {
        return userRatings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(city);
        parcel.writeStringList(images);
        parcel.writeTypedList(userRatings);
    }
}
