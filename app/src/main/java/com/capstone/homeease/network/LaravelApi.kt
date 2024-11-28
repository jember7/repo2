package com.capstone.homeease.network

import com.capstone.homeease.model.ApiResponse
import com.capstone.homeease.model.Booking
import com.capstone.homeease.model.BookingRequest
import com.capstone.homeease.model.Expert
import com.capstone.homeease.model.ExpertIdResponse
import com.capstone.homeease.model.ExpertProfileResponse
import com.capstone.homeease.model.LoginRequest
import com.capstone.homeease.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LaravelApi {
    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET("user-profile")
    fun getUserProfile(
        @Query("id") userId: Int // Use @Query to pass the userId as a query parameter
    ): Call<LoginResponse>
    @GET("expert-profile")
    fun getExpertProfile(@Query("userId") userId: Int): Call<ExpertProfileResponse>
    @GET("experts")
    fun getExpertsByProfession(@Query("profession") profession: String): Call<List<Expert>>
    @POST("bookings")
    fun bookExpert(@Body bookingRequest: BookingRequest): Call<ApiResponse>
    @GET("expert-profile")
    fun getExpertIdByEmail(
        @Query("email") email: String
    ): Call<ExpertIdResponse>


    @GET("expert/{userId}/bookings")
    fun getExpertBookings(@Path("userId") userId: Int): Call<List<Booking>>

    // Endpoint to get ongoing bookings
    @GET("expert/{userId}/ongoing-bookings")
    fun getOngoingBookings(@Path("userId") userId: Int): Call<List<Booking>>


}
