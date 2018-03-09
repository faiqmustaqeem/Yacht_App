package com.digitalexperts.bookyachts.interfaces;

import com.google.gson.JsonElement;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by free on 2/9/2017.
 */

public interface WebServices {

    @POST("user/register")
    @Headers("Content-Type:application/json")
    public Call<Object> registerUser(@Body HashMap<String, String> body);

    @POST("user/update")
    @Headers("Content-Type:application/json")
    public Call<Object> updateUser(@Body HashMap<String, String> body);

    @POST("user/login")
    @Headers("Content-Type:application/json")
    public Call<Object> loginUser(@Body HashMap<String, String> body);

    @POST("yacht/AllYacht")
    @Headers("Content-Type:application/json")
    public Call<Object> getAllYachts(@Body HashMap<String, String> body);

    @POST("book/CallBack")
    @Headers("Content-Type:application/json")
    public Call<Object> callBack(@Body HashMap<String, String> body);

    @POST("yacht/GetAYacht")
    @Headers("Content-Type:application/json")
    public Call<Object> yachtDetails(@Body HashMap<String, String> body);

    @POST("book/Booking")
    @Headers("Content-Type:application/json")
    public Call<Object> booking(@Body HashMap<String, String> body);

    @POST("book/BookingHistory")
    @Headers("Content-Type:application/json")
    public Call<Object> bookingHistory(@Body HashMap<String, String> body);

    @POST("book/Booked_Times")
    @Headers("Content-Type:application/json")
    public Call<Object> fetchBookedTimes(@Body HashMap<String, String> body);

    @POST("contact/Contact_Info")
    @Headers("Content-Type:application/json")
    public Call<Object> getContactDetails(@Body HashMap<String, String> body);

    @POST("hot_deal/Hot_Deal")
    @Headers("Content-Type:application/json")
    public Call<Object> getHotDeals(@Body HashMap<String, String> body);

    @POST("contact/Contact_Form")
    @Headers("Content-Type:application/json")
    public Call<Object> submitMessage(@Body HashMap<String, String> body);

    @POST("user/Countries")
    @Headers("Content-Type:application/json")
    public Call<Object> fetchCountries(@Body HashMap<String, String> body);

    @POST("user/states")
    @Headers("Content-Type:application/json")
    public Call<Object> fetchStates(@Body HashMap<String, String> body);

    @POST("user/cities")
    @Headers("Content-Type:application/json")
    public Call<Object> fetchCities(@Body HashMap<String, String> body);

    @POST("user/reset_password")
    @Headers("Content-Type:application/json")
    public Call<Object> sendEmail(@Body HashMap<String, String> body);

    @POST("book/Status_Booking")
    @Headers("Content-Type:application/json")
    public Call<Object> statusChange(@Body HashMap<String, String> body);



    @POST("book/Booking_check")
    @Headers("Content-Type:application/json")
    public Call<Object> checkTimeAvailability(@Body HashMap<String, String> body);
}