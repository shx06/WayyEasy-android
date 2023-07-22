package com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers;

import com.wayyeasy.wayyeasy.Models.Hospitals.beds.beds_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.doctors_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.hospital_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.hospitals_ratings_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.search_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.userVerify.user_verify_response_model;
import com.wayyeasy.wayyeasy.Models.PathLabs.pathLabs.path_lab_response_model;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_booking_response_response_model;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.Models.Physicians.physicians_ratings_response_model;
import com.wayyeasy.wayyeasy.Models.user.fetched_physician_list_response_mode;
import com.wayyeasy.wayyeasy.Models.user.physicians_response_model;
import com.wayyeasy.wayyeasy.Models.user.user_response_model;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiSet {


    //for user profile
    @POST("user/signup")
    Call<Void> generateOTP(@Body HashMap<String, String> map);

    @POST("user/verifyOTP")
    Call<user_verify_response_model> login(@Body HashMap<String, String> map);

    @GET("user/getUser")
    Call<user_response_model> fetchUser(@Header("Authorization") String token);

    @PATCH("user/update")
    Call<user_response_model> updateProfile(@Header("Authorization") String token,
                                            @Body HashMap<String, String> map);

    @PATCH("user/updateFCMToken")
    Call<user_response_model> updateProfileToken(@Header("Authorization") String token,
                                                 @Body HashMap<String, String> map);





    /*---------------------------------------------------------------------------------------------------------------------------*/

    //for hospital section
    @GET("hospital/userView")
    Call<List<hospital_response_model>> getFilteredHospital(@Header("Authorization") String token,
                                                            @Query("name") String name,
                                                            @Query("value") String value,
                                                            @Query("limit") int limit,
                                                            @Query("page") int page);

    @GET("hospital/userView")
    Call<List<search_response_model>> searchHospitals(@Header("Authorization") String token,
                                                      @Query("name") String name,
                                                      @Query("limit") int limit,
                                                      @Query("page") int page);

    @GET("room/getRoomsByHospital/{hospitalId}")
    Call<List<beds_response_model>> getRoomsByHospital(@Header("Authorization") String token,
                                                       @Path("hospitalId") String hospitalId);

    @GET("doctor/getDoctorsByHospital/{hospitalId}")
    Call<List<doctors_response_model>> getDoctorsByHospital(@Header("Authorization") String token,
                                                            @Path("hospitalId") String hospitalId);

    @GET("ratings/getRatingsByHospital/{hospitalId}")
    Call<List<hospitals_ratings_response_model>> getRatingsByHospital(@Header("Authorization") String token,
                                                                      @Path("hospitalId") String hospitalId);



    /*---------------------------------------------------------------------------------------------------------------------------*/

    //Path Labs
    @GET("pathLabs/viewAll")
    Call<List<path_lab_response_model>> getAllPathLabs(@Header("Authorization") String token);



    /*---------------------------------------------------------------------------------------------------------------------------*/

    //Physicians

    //search physician by type
    @GET("physicians/getPhysicianByType")
    Call<List<physician_response_model>> getPhysiciansByType(@Header("Authorization") String token,
                                                             @Path("dataType") String dataType,
                                                             @Query("limit") int limit,
                                                             @Query("page") int page);


    //All physicians are fetched
    @GET("physicians/getPhysician/{dataType}")
    Call<List<physician_response_model>> getPhysicians(@Header("Authorization") String token,
                                                       @Path("dataType") String dataType,
                                                       @Query("limit") int limit,
                                                       @Query("page") int page);

    @GET("physicians/getPhysician/{dataType}")
    Call<List<physician_response_model>> getPhysiciansBySearch(@Header("Authorization") String token,
                                                               @Query("searchQuery") String searchQuery,
                                                               @Query("limit") int limit,
                                                               @Query("page") int page);

    //get consultation status
    @GET("physicianBookings/fetchPhysicianConsultationStatus/{physicianId}")
    Call<physician_booking_response_response_model> getConsultation(@Header("Authorization") String token,
                                                                    @Path("physicianId") String physicianId);


    //user will book physician
    @POST("physicianBookings/bookPhysician/{physicianId}")
    Call<physician_booking_response_response_model> bookPhysicianByUser(@Header("Authorization") String token,
                                                                        @Path("physicianId") String physicianId);

    //List of booked physicians by user
    @GET("physicianBookings/fetchAllPhysiciansByUser/{dataType}")
    Call<fetched_physician_list_response_mode> getBookedPhysicians(@Header("Authorization") String token,
                                                                   @Path("dataType") String dataType);

    //get prescription
    @GET("physicianPrescription/getByUser/{physicianId}")
    Call<physicians_response_model> getPrescription(@Header("Authorization") String token,
                                                    @Path("physicianId") String physicianId);

    //finish booking
    @PATCH("physicianBookings/finishConsultation/{physicianId}")
    Call<Void> finishConsultation(@Header("Authorization") String token,
                                  @Path("physicianId") String physicianId);

    //Physicians Ratings
    @POST("user/ratings/{physicianId}")
    Call<Void> ratePhysician(@Header("Authorization") String token,
                             @Path("physicianId") String physicianId,
                             @Body HashMap<String, physicians_ratings_response_model> data);
}
