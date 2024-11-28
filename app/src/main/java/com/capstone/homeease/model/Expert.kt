package com.capstone.homeease.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ExpertProfileResponse(
    val message: String,
    @SerializedName("id") val userId: Int,
    val full_name: String,
    val email: String,
    val profession: String,
    val date_of_birth: String,
    val address: String,
    val phone_number: String,
    val profile_image: String?,
    val role: String,
    val created_at: String,
    val updated_at: String
)
data class Expert(
    @SerializedName("full_name")val fullName: String,
    val email: String,
    val password: String,
    val passwordConfirmation: String,
    val profession: String,
    val dateOfBirth: String,
    val address: String,
    val number: String,
    val profileImage: Uri? = null,
    val role: String = "expert" // Default role is expert
)

