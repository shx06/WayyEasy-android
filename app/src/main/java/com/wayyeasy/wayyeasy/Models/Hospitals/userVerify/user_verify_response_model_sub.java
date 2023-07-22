package com.wayyeasy.wayyeasy.Models.Hospitals.userVerify;

public class user_verify_response_model_sub {
    private String number, role, _id, fcmToken;

    public user_verify_response_model_sub() {
    }

    public user_verify_response_model_sub(String number, String role, String _id, String fcmToken) {
        this.number = number;
        this.role = role;
        this._id = _id;
        this.fcmToken = fcmToken;
    }

    public String getNumber() {
        return number;
    }

    public String getRole() {
        return role;
    }

    public String get_id() {
        return _id;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
