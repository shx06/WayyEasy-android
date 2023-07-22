package com.wayyeasy.wayyeasy.Models.Hospitals.userVerify;

public class user_verify_response_model {
    private String token, message;
    private user_verify_response_model_sub data;

    public user_verify_response_model() {
    }

    public user_verify_response_model(String token, String message, user_verify_response_model_sub user_verify_response_model_sub) {
        this.token = token;
        this.message = message;
        data = user_verify_response_model_sub;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public user_verify_response_model_sub getData() {
        return data;
    }
}
