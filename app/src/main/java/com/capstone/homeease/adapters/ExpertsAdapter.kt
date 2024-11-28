package com.capstone.homeease.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.homeease.R
import com.capstone.homeease.model.ApiResponse
import com.capstone.homeease.model.BookingRequest
import com.capstone.homeease.model.Expert
import com.capstone.homeease.model.ExpertIdResponse
import com.capstone.homeease.network.LaravelApi
import com.capstone.homeease.utils.SharedPreferencesHelper.getUserIdFromSession
import com.capstone.homeease.utils.SharedPreferencesHelper.getUserNameFromSession
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class ExpertsAdapter(
    private val context: Context,
    private var experts: List<Expert> // Make this a mutable list so we can update it
) : RecyclerView.Adapter<ExpertsAdapter.ExpertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_expert, parent, false)
        return ExpertViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpertViewHolder, position: Int) {
        val expert = experts[position]
        Log.d("ExpertsAdapter", "Expert Full Name: ${expert.fullName}, Address: ${expert.address}")
        holder.fullNameTextView.text = expert.fullName
        holder.addressTextView.text = expert.address

        // Load expert profile image if available
        expert.profileImage?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.img) // Add placeholder
                .into(holder.profileImageView)
        }

        // Set up the Book Now button click listener
        holder.bookNowButton.setOnClickListener {
            val userId = getUserIdFromSession(context)
            val userName = getUserNameFromSession(context)
            if (userId == null) {
                Toast.makeText(context, "Please log in to book an expert", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gson: Gson = GsonBuilder().setLenient().create()


            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()


            val apiService = retrofit.create(LaravelApi::class.java)


            // Fetch the expert's ID by email
            apiService.getExpertIdByEmail(expert.email).enqueue(object : Callback<ExpertIdResponse> {
                override fun onResponse(call: Call<ExpertIdResponse>, response: Response<ExpertIdResponse>) {
                    if (response.isSuccessful) {
                        val expertId = response.body()?.id
                        Log.d("API Response", "Expert: $expert")

                        if (expertId != null) {
                            // Convert current time to a formatted date string
                            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                            Log.d("BookingRequest", "User Name: $userName")

                            // Create the booking data
                            val bookingData = BookingRequest(
                                userId = userId,
                                expertId = expertId,
                                expertName = expert.fullName,
                                expertAddress = expert.address,
                                userAddress = "address",
                                userName = "name", // Corrected to user's name
                                status = "Pending",
                                timestamp = timestamp
                            )

                            // Send the booking request to the backend
                            apiService.bookExpert(bookingData).enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                    if (response.isSuccessful) {
                                        Log.d("Booking Success", "Response: ${response.body()}")
                                        Toast.makeText(context, "Booking request sent successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.e("Booking Error", "Error: ${response.errorBody()?.string()}")
                                        Log.e("ExpertsAdapter", "Error creating booking: ${response.errorBody()?.string()}")
                                        Toast.makeText(context, "Failed to send booking request", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                    Log.e("ExpertsAdapter", "Network error: ${t.message}")
                                    Toast.makeText(context, "Network error, please try again", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            Toast.makeText(context, "Failed to fetch expert ID", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ExpertsAdapter", "Error fetching expert ID: ${response.errorBody()?.string()}")
                        Toast.makeText(context, "Failed to fetch expert details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ExpertIdResponse>, t: Throwable) {
                    Log.e("ExpertsAdapter", "Network error: ${t.message}")
                    Toast.makeText(context, "Network error, please try again", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    override fun getItemCount(): Int = experts.size


    fun updateExperts(expertsList: List<Expert>) {
        experts = expertsList
        notifyDataSetChanged()
    }

    class ExpertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.address)
        val profileImageView: ImageView = itemView.findViewById(R.id.expertImageView)
        val bookNowButton: Button = itemView.findViewById(R.id.bookNowButton)
    }
}
