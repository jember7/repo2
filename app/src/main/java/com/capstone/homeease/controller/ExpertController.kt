package com.capstone.homeease.controller

import com.capstone.homeease.network.RetrofitClient
import com.capstone.homeease.view.ExpertView
import com.capstone.homeease.model.ExpertRepository
import com.capstone.homeease.network.LaravelApi

class ExpertController(private val view: ExpertView) {

    private val repository: ExpertRepository = ExpertRepository(
        RetrofitClient.createService(LaravelApi::class.java)
    )

    fun fetchExperts(profession: String) {
        repository.getExperts(profession) { experts, error ->
            if (experts != null) {
                view.showExperts(experts)
            } else {
                view.showError(error ?: "Unknown error")
            }
        }
    }
}
