package com.wayyeasy.wayyeasy.Models.user;

import java.util.List;

public class user_response_model {
    private String _id, name, address, age, email, sex, profileImage, city, pinCode;
    List<physicians_response_model> physiciansBooked;

    public user_response_model() {
    }

    public user_response_model(String _id, String name, String address, String age, String email, String sex, String profileImage, String city, String pinCode, List<physicians_response_model> physiciansBooked) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.email = email;
        this.sex = sex;
        this.profileImage = profileImage;
        this.city = city;
        this.pinCode = pinCode;
        this.physiciansBooked = physiciansBooked;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getCity() {
        return city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public List<physicians_response_model> getPhysiciansBooked() {
        return physiciansBooked;
    }
}
