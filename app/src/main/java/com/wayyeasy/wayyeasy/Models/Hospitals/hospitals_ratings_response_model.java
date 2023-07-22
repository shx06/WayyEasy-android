package com.wayyeasy.wayyeasy.Models.Hospitals;

public class hospitals_ratings_response_model {
    Float staffs, clean, treatement, location, valueForMoney, average;
    String review, userName;
    public hospitals_ratings_response_model() {
    }

    public hospitals_ratings_response_model(Float staffs, Float clean, Float treatement, Float location, Float valueForMoney, Float average, String review, String userName) {
        this.staffs = staffs;
        this.clean = clean;
        this.treatement = treatement;
        this.location = location;
        this.valueForMoney = valueForMoney;
        this.average = average;
        this.review = review;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getReview() {
        return review;
    }

    public float getStaffs() {
        return staffs;
    }

    public float getClean() {
        return clean;
    }

    public float getTreatement() {
        return treatement;
    }

    public float getLocation() {
        return location;
    }

    public float getValueForMoney() {
        return valueForMoney;
    }

    public float getAverage() {
        return average;
    }
}
