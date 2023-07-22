package com.wayyeasy.wayyeasy.ComponentFiles.Constants;

import java.util.ArrayList;

public class Images {
    private static ArrayList<String> hospitalImages;
    private static ArrayList<String> roomImages;
    private static String physiciansImage;

    public Images() {
    }

    public Images(ArrayList<String> hospitalImages, ArrayList<String> roomImages, String physiciansImage) {
        this.hospitalImages = hospitalImages;
        this.roomImages = roomImages;
        this.physiciansImage = physiciansImage;
    }

    public static ArrayList<String> getHospitalImages() {
        return hospitalImages;
    }

    public static void setHospitalImages(ArrayList<String> hospitalImages) {
        Images.hospitalImages = hospitalImages;
    }

    public static ArrayList<String> getRoomImages() {
        return roomImages;
    }

    public static void setRoomImages(ArrayList<String> roomImages) {
        Images.roomImages = roomImages;
    }

    public static String getPhysiciansImage() {
        return physiciansImage;
    }

    public static void setPhysiciansImage(String physiciansImage) {
        Images.physiciansImage = physiciansImage;
    }
}
