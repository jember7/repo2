package com.capstone.homeease.network

import com.capstone.homeease.model.LoginRequest
import com.capstone.homeease.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface ApiService {

    @Multipart
    @POST("register/expert")  // Ensure this matches the endpoint in Laravel
    fun registerExpertWithImage(
        @PartMap fields: HashMap<String, String>,
        @Part image: MultipartBody.Part?
    ): Call<ResponseBody>


    companion object {
        private const val BASE_URL = "http://10.0.2.2:8000/api/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

