package com.wayyeasy.wayyeasy.Models.Hospitals;

import java.util.ArrayList;

public class hospital_response_model {
    String owner, _id, name, address, contactNumber, description, pincode;
    private ArrayList<String> images;

    public hospital_response_model() {
    }

    public hospital_response_model(String owner, String _id, String name, String address, String contactNumber, String description, String pincode, ArrayList<String> images) {
        this.owner = owner;
        this._id = _id;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.description = description;
        this.pincode = pincode;
        this.images = images;
    }

    public String getOwner() {
        return owner;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getPincode() {
        return pincode;
    }

    public ArrayList<String> getImages() {
        return images;
    }

}
