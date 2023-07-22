package com.wayyeasy.wayyeasy.Models.Physicians;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class physician_response_model implements Parcelable {
    private String role, _id, name, email, isFull, status, fcmToken, address, description, qualification, shiftEnd, shiftStart, specialityType, mobile, price, image, badge, workingDays;
    boolean available;

    public physician_response_model() {
    }

    public physician_response_model(String role, String _id, String name, String email, String isFull, String status, String fcmToken, String address, String description, String qualification, String shiftEnd, String shiftStart, String specialityType, String mobile, String price, String image, String badge, String workingDays, boolean available) {
        this.role = role;
        this._id = _id;
        this.name = name;
        this.email = email;
        this.isFull = isFull;
        this.status = status;
        this.fcmToken = fcmToken;
        this.address = address;
        this.description = description;
        this.qualification = qualification;
        this.shiftEnd = shiftEnd;
        this.shiftStart = shiftStart;
        this.specialityType = specialityType;
        this.mobile = mobile;
        this.price = price;
        this.image = image;
        this.badge = badge;
        this.workingDays = workingDays;
        this.available = available;
    }

    protected physician_response_model(Parcel in) {
        role = in.readString();
        _id = in.readString();
        name = in.readString();
        email = in.readString();
        isFull = in.readString();
        status = in.readString();
        fcmToken = in.readString();
        address = in.readString();
        description = in.readString();
        qualification = in.readString();
        shiftEnd = in.readString();
        shiftStart = in.readString();
        specialityType = in.readString();
        mobile = in.readString();
        price = in.readString();
        image = in.readString();
        badge = in.readString();
        workingDays = in.readString();
        available = in.readByte() != 0;
    }

    public static final Creator<physician_response_model> CREATOR = new Creator<physician_response_model>() {
        @Override
        public physician_response_model createFromParcel(Parcel in) {
            return new physician_response_model(in);
        }

        @Override
        public physician_response_model[] newArray(int size) {
            return new physician_response_model[size];
        }
    };

    public String getRole() {
        return role;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getIsFull() {
        return isFull;
    }

    public String getStatus() {
        return status;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getQualification() {
        return qualification;
    }

    public String getShiftEnd() {
        return shiftEnd;
    }

    public String getShiftStart() {
        return shiftStart;
    }

    public String getSpecialityType() {
        return specialityType;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getBadge() {
        return badge;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(role);
        parcel.writeString(_id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(isFull);
        parcel.writeString(status);
        parcel.writeString(fcmToken);
        parcel.writeString(address);
        parcel.writeString(description);
        parcel.writeString(qualification);
        parcel.writeString(shiftEnd);
        parcel.writeString(shiftStart);
        parcel.writeString(specialityType);
        parcel.writeString(mobile);
        parcel.writeString(price);
        parcel.writeString(image);
        parcel.writeString(badge);
        parcel.writeString(workingDays);
        parcel.writeByte((byte) (available ? 1 : 0));
    }
}
