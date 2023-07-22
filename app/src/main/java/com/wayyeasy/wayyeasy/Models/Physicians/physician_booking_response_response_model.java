package com.wayyeasy.wayyeasy.Models.Physicians;

public class physician_booking_response_response_model {
    private String message, _id, physicianId, userId, consultation;

    public physician_booking_response_response_model() {
    }

    public physician_booking_response_response_model(String message, String _id, String physicianId, String userId, String consultation) {
        this.message = message;
        this._id = _id;
        this.physicianId = physicianId;
        this.userId = userId;
        this.consultation = consultation;
    }

    public String getMessage() {
        return message;
    }

    public String get_id() {
        return _id;
    }

    public String getPhysicianId() {
        return physicianId;
    }

    public String getUserId() {
        return userId;
    }

    public String getConsultation() {
        return consultation;
    }
}
