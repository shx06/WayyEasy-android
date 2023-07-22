package com.wayyeasy.wayyeasy.ComponentFiles.Constants;

public class HospitalBooking {
    public static String hospitalId, RoomId, roomNo, roomType, hospitalOwner, hospitalName;

    public static String getHospitalName() {
        return hospitalName;
    }

    public static void setHospitalName(String hospitalName) {
        HospitalBooking.hospitalName = hospitalName;
    }

    public static String getHospitalId() {
        return hospitalId;
    }

    public static void setHospitalId(String hospitalId) {
        HospitalBooking.hospitalId = hospitalId;
    }

    public static String getRoomId() {
        return RoomId;
    }

    public static void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public static String getRoomNo() {
        return roomNo;
    }

    public static void setRoomNo(String roomNo) {
        HospitalBooking.roomNo = roomNo;
    }

    public static String getRoomType() {
        return roomType;
    }

    public static void setRoomType(String roomType) {
        HospitalBooking.roomType = roomType;
    }

    public static String getHospitalOwner() {
        return hospitalOwner;
    }

    public static void setHospitalOwner(String hospitalOwner) {
        HospitalBooking.hospitalOwner = hospitalOwner;
    }
}
