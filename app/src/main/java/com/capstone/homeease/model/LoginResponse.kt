package com.capstone.homeease.model

data class LoginResponse(
    val message: String,
    val role: String,
    val user_id: Int,
    val address: String?,
    val email: String?,
    val number: Number?,
    val fullName: String?,
    val profileImage: String?
)

