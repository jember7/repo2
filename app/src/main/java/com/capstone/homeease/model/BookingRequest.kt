package com.capstone.homeease.model

data class BookingRequest(
    val userId: Int,
    val expertId: Int,
    val expertName: String,
    val userName: String,
    val expertAddress: String,
    val userAddress: String,
    val status: String,
    val timestamp: String // Ensure this is a properly formatted date string, not a long
)