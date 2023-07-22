package com.wayyeasy.wayyeasy.Models.OnlineCalls;

public class call_response_model {
    private String id, name, image, role, status, date;

    public call_response_model() {
    }

    public call_response_model(String id, String name, String image, String role, String status, String date) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = date;
        this.image = image;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }
}
