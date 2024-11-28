package com.capstone.homeease.model

data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class ExpertIdResponse(
    val id: Int
)