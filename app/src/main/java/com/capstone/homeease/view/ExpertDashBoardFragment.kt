package com.capstone.homeease.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.homeease.R
import com.capstone.homeease.adapters.ExpertBookingsAdapter
import com.capstone.homeease.adapters.OngoingBookingsAdapter
import com.capstone.homeease.databinding.FragmentExpertDashBoardBinding
import com.capstone.homeease.model.Booking
import com.capstone.homeease.model.ExpertProfileResponse
import com.capstone.homeease.network.LaravelApi
import com.capstone.homeease.utils.SharedPreferencesHelper.getUserId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExpertDashBoardFragment : Fragment(R.layout.fragment_expert_dash_board) {

    private lateinit var binding: FragmentExpertDashBoardBinding
    private lateinit var expertBookingsAdapter: ExpertBookingsAdapter
    private lateinit var ongoingBookingsAdapter: OngoingBookingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpertDashBoardBinding.inflate(inflater, container, false)

        // Initialize RecyclerViews
        binding.bookingsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.ongoingBookingsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Initialize both adapters
        expertBookingsAdapter = ExpertBookingsAdapter(requireContext(), emptyList())
        ongoingBookingsAdapter = OngoingBookingsAdapter(requireContext(), emptyList())

        // Set the adapters for the RecyclerViews
        binding.bookingsRecyclerView.adapter = expertBookingsAdapter
        binding.ongoingBookingsRecyclerView.adapter = ongoingBookingsAdapter

        // Set click listeners for ImageViews (Navigation)
        setNavigationListeners()

        // Load expert profile and bookings
        loadExpertProfile()
        fetchBookings()
        fetchOngoingBookings()

        return binding.root
    }

    private fun setNavigationListeners() {
        binding.activity.setOnClickListener {
            // Navigate to Expert Activity Page
        }

        binding.payment.setOnClickListener {
            // Navigate to Expert Payment Page
        }

        binding.textHome.setOnClickListener {
            // Reload Expert Dashboard
        }

        binding.messages.setOnClickListener {
            // Navigate to Expert Messages Page
        }

        binding.profile.setOnClickListener {
            // Navigate to Expert Profile Page
        }
    }

    private fun loadExpertProfile() {
        val userId = getUserId(requireContext())
        if (userId == -1) {
            Log.e("ExpertDashBoardFragment", "User ID not found")
            return
        }

        // Make Retrofit call to fetch the expert profile
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(LaravelApi::class.java)

        api.getExpertProfile(userId).enqueue(object : Callback<ExpertProfileResponse> {
            override fun onResponse(
                call: Call<ExpertProfileResponse>,
                response: Response<ExpertProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val expert = response.body()
                    Log.d("ExpertDashBoardFragment", "Expert Profile: $expert")
                    Log.d("ExpertDashBoardFragment", "Full name: ${expert?.full_name}, Number: ${expert?.phone_number}")

                    // Update the UI with the expert's data
                    binding.usernameText.text = expert?.full_name ?: "Expert Name"
                    binding.numberText.text = expert?.phone_number ?: "Add Phone Number"

                    expert?.profile_image?.let {
                        Glide.with(requireContext())
                            .load(it)
                            .into(binding.profilePicture)
                    }

                } else {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExpertProfileResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }





    private fun fetchBookings() {
        val userId = getUserId(requireContext())
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(LaravelApi::class.java)

        api.getExpertBookings(userId).enqueue(object : Callback<List<Booking>> {
            override fun onResponse(call: Call<List<Booking>>, response: Response<List<Booking>>) {
                if (response.isSuccessful) {
                    val bookings = response.body() ?: emptyList()
                    expertBookingsAdapter.updateBookings(bookings)
                } else {
                    Toast.makeText(requireContext(), "Failed to load bookings", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<Booking>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchOngoingBookings() {
        val userId = getUserId(requireContext())
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(LaravelApi::class.java)

        api.getOngoingBookings(userId).enqueue(object : Callback<List<Booking>> {
            override fun onResponse(call: Call<List<Booking>>, response: Response<List<Booking>>) {
                if (response.isSuccessful) {
                    val ongoingBookings = response.body() ?: emptyList()
                    ongoingBookingsAdapter.updateBookings(ongoingBookings)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load ongoing bookings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Booking>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
