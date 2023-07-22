package com.wayyeasy.wayyeasy.Models.Hospitals;

import java.util.ArrayList;

public class search_response_model {
    String _id, name, address, contactNumber, description, pincode;
    private ArrayList<String> images;

    public search_response_model() {
    }

    public search_response_model(String _id, String name, String address, String contactNumber, String description, String pincode, ArrayList<String> images) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.description = description;
        this.pincode = pincode;
        this.images = images;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
