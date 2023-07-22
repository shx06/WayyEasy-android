package com.wayyeasy.wayyeasy.ComponentFiles.Constants;

import java.util.HashMap;

public class Constants {


    /*   <------------------------------------------------------------------------------------------------>  */
    //static constants
    public static String mobileEntered = null;


    /*   <------------------------------------------------------------------------------------------------>  */
    //final constants


    //incoming caller tune
    public static final String PLAY_RINGTONE = "PLAY_RINGTONE";
    public static final String RECEIVE_CALL = "RECEIVE_CALL";
    public static final String REJECT_CALL = "REJECT_CALL";
    public static final String CALL_CHECKED = "CALL_CHECKED";
    public static final String CALL_MISSED = "CALL_MISSED";
    public static final String CALLING_PERSON = "CALLING_PERSON";
    public static final String CALLING_PERSON_MONGO_ID = "CALLING_PERSON_MONGO_ID";
    public static final String CALLING_PERSON_IMAGE = "CALLING_PERSON_IMAGE";
    public static final String CALLING_PERSON_ROLE = "CALLING_PERSON_ROLE";


    //app local constants
    public static final String policiesLink = "policiesLink";

    //firebase
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_FIREBASE_USER_ID = "firebaseId";

    //mongodb users data
    public static final String name = "name";
    public static final String age = "age";
    public static final String sex = "sex";
    public static final String address = "address";
    public static final String city = "city";
    public static final String role = "role";
    public static final String pinCode = "pinCode";
    public static final String email = "email";
    public static final String profileImage = "profileImage";
    public static final String amountPaid = "amountPaid";
    public static final String otp = "otp";
    public static final String speciality = "specialityType";
    public static final String KEY_AUTHENTICATED_NUMBER = "number";
    public static final String KEY_MONGO_TOKEN = "mongoToken";
    public static final String KEY_MONGO_ID = "mongoId";

    //physicians
    public static final String physician = "physician";
    public static final String physicianData = "physicianData";
    public static final String physicianId = "physicianId";
    public static final String physicianName = "physicianName";
    public static final String pending = "pending";
    public static final String finished = "finished";
    public static final String prescription = "prescription";
    public static final String containPrescription = "containPrescription";
    public static final String fetchPhysicianConsultationType = "fetchPhysicianConsultationType";
    public static final String physiciansRatings = "physiciansRatings";
    public static final String physicianSearchType = "physicianSearchType";


    //shared preferences
    public static final String KEY_PREFERENCE_NAME_USER = "loginCredentials";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_PREFERENCE_CALL = "callingCredentials";

    //remote messaging
    public static final String REMOTE_MESSAGE_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MESSAGE_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MESSAGE_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MESSAGE_INVITER_TOKEN = "inviter_token";
    public static final String REMOTE_MESSAGE_DATA = "data";
    public static final String REMOTE_MESSAGE_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "invitationAccepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "invitationRejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "invitationCancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";

    public static final String JITSI_MEET_SERVER_URL = "https://meet.jit.si";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MESSAGE_AUTHORIZATION,
                "key=AAAAwZdntJM:APA91bHxfvNFv-EGjAIk4JFajCHkN_ZDjS09l-7DUhnw9ApwD3ALn12w-RgkL1cAMiQLr2WzuBgEkLr1edlWErCRoYyCWnyqDvI0sCWv-WlU2FeCuxv55K5lZNtZSQ5a_yDodscFA88D");
        headers.put(Constants.REMOTE_MESSAGE_CONTENT_TYPE, "application/json");
        return headers;
    }
}
