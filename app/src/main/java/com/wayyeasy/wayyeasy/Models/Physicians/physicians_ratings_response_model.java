package com.wayyeasy.wayyeasy.Models.Physicians;

public class physicians_ratings_response_model {
    private String username, userImage, review, physicianId, status;
    private float rating;

    public physicians_ratings_response_model() {
    }

    public physicians_ratings_response_model(String username, String userImage, String review, String physicianId, String status, float rating) {
        this.username = username;
        this.userImage = userImage;
        this.review = review;
        this.physicianId = physicianId;
        this.status = status;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(String physicianId) {
        this.physicianId = physicianId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
