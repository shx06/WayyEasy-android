package com.wayyeasy.wayyeasy.Models.PathLabs.pathLabs;

import android.os.Parcel;
import android.os.Parcelable;

public class path_lab_ratings_response_model implements Parcelable {
    private String userId, name, ratings, _id;

    public path_lab_ratings_response_model() {
    }

    public path_lab_ratings_response_model(String userId, String name, String ratings, String _id) {
        this.userId = userId;
        this.name = name;
        this.ratings = ratings;
        this._id = _id;
    }

    protected path_lab_ratings_response_model(Parcel in) {
        userId = in.readString();
        name = in.readString();
        ratings = in.readString();
        _id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(ratings);
        dest.writeString(_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<path_lab_ratings_response_model> CREATOR = new Creator<path_lab_ratings_response_model>() {
        @Override
        public path_lab_ratings_response_model createFromParcel(Parcel in) {
            return new path_lab_ratings_response_model(in);
        }

        @Override
        public path_lab_ratings_response_model[] newArray(int size) {
            return new path_lab_ratings_response_model[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRatings() {
        return ratings;
    }

    public String get_id() {
        return _id;
    }
}
