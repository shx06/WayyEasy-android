package com.wayyeasy.wayyeasy.Models.Hospitals;

public class bed_no_response_model {

    private String roomNo, available, _id;

    public bed_no_response_model() {
    }

    public bed_no_response_model(String roomNo, String available, String _id) {
        this.roomNo = roomNo;
        this.available = available;
        this._id = _id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
