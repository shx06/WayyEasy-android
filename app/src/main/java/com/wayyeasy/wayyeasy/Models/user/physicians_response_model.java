package com.wayyeasy.wayyeasy.Models.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.wayyeasy.wayyeasy.Models.Physicians.prescription_response_model;
import java.util.ArrayList;

public class physicians_response_model implements Parcelable {
    private String _id, physicianId, userId, consultationId, createdAt, updatedAt;

    ArrayList<prescription_response_model> prescription;

    public physicians_response_model() {
    }

    public physicians_response_model(String _id, String physicianId, String userId, String consultationId, String createdAt, String updatedAt, ArrayList<prescription_response_model> prescription) {
        this._id = _id;
        this.physicianId = physicianId;
        this.userId = userId;
        this.consultationId = consultationId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.prescription = prescription;
    }

    protected physicians_response_model(Parcel in) {
        _id = in.readString();
        physicianId = in.readString();
        userId = in.readString();
        consultationId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        prescription = in.createTypedArrayList(prescription_response_model.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(physicianId);
        dest.writeString(userId);
        dest.writeString(consultationId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeTypedList(prescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<physicians_response_model> CREATOR = new Creator<physicians_response_model>() {
        @Override
        public physicians_response_model createFromParcel(Parcel in) {
            return new physicians_response_model(in);
        }

        @Override
        public physicians_response_model[] newArray(int size) {
            return new physicians_response_model[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public String getPhysicianId() {
        return physicianId;
    }

    public String getUserId() {
        return userId;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ArrayList<prescription_response_model> getPrescription() {
        return prescription;
    }
}
