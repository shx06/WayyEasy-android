package com.wayyeasy.wayyeasy.Models.Hospitals.bookings;

public class room_book_request_user_to_owner {
    public String hospitalId, RoomId, roomNo, roomType, hospitalOwner, userId, userName;

    public room_book_request_user_to_owner() {
    }

    public room_book_request_user_to_owner(String hospitalId, String roomId, String roomNo, String roomType, String hospitalOwner, String userId, String userName) {
        this.hospitalId = hospitalId;
        RoomId = roomId;
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.hospitalOwner = hospitalOwner;
        this.userId = userId;
        this.userName = userName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getHospitalOwner() {
        return hospitalOwner;
    }

    public void setHospitalOwner(String hospitalOwner) {
        this.hospitalOwner = hospitalOwner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
