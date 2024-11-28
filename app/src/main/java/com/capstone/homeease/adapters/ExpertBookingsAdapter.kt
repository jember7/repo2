package com.capstone.homeease.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.homeease.R
import com.capstone.homeease.model.Booking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpertBookingsAdapter(private val context: Context, private var bookings: List<Booking>) : RecyclerView.Adapter<ExpertBookingsAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookings, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]

        // Bind data to the views
        holder.expertNameTextView.text = booking.userName
        holder.address.text = "Address: ${booking.userAddress}"
        holder.bookingStatusTextView.text = "Status: ${booking.status}"
        holder.note.text = "Note: ${booking.note}"
        holder.bookingTimestampTextView.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(
            Date(booking.timestamp)
        )

        // Show and hide buttons based on the booking status
        when (booking.status) {
            "Pending" -> {
                holder.acceptButton.visibility = View.VISIBLE
                holder.declineButton.visibility = View.VISIBLE
                holder.completeButton.visibility = View.GONE
                holder.cancelButton.visibility = View.GONE
            }
            "Accepted" -> {
                holder.acceptButton.visibility = View.GONE
                holder.declineButton.visibility = View.GONE
                holder.completeButton.visibility = View.VISIBLE
                holder.cancelButton.visibility = View.GONE
            }
            else -> {
                holder.acceptButton.visibility = View.GONE
                holder.declineButton.visibility = View.GONE
                holder.completeButton.visibility = View.GONE
                holder.cancelButton.visibility = View.GONE
            }
        }

        holder.acceptButton.setOnClickListener {
            updateBookingStatus(booking, "Accepted")
        }

        holder.declineButton.setOnClickListener {
            updateBookingStatus(booking, "Declined")
        }

        holder.completeButton.setOnClickListener {
            updateBookingStatus(booking, "Completed")
        }
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    fun updateBookings(newBookings: List<Booking>) {
        this.bookings = newBookings
        notifyDataSetChanged()
    }

    private fun updateBookingStatus(booking: Booking, status: String) {
        // Assuming you have a Retrofit service to update the status on the server
        // You can implement a Retrofit call here to update the status

        // Simulating a network call (you can replace this with actual API call)
        val updatedBookings = bookings.toMutableList()
        val index = updatedBookings.indexOfFirst { it.id == booking.id }
        if (index != -1) {
            val updatedBooking = booking.copy(status = status)
            updatedBookings[index] = updatedBooking
            bookings = updatedBookings
            notifyItemChanged(index)
            Toast.makeText(context, "Booking $status", Toast.LENGTH_SHORT).show()
        }
    }

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expertNameTextView: TextView = itemView.findViewById(R.id.expertNameTextView)
        val address: TextView = itemView.findViewById(R.id.address)
        val bookingStatusTextView: TextView = itemView.findViewById(R.id.bookingStatusTextView)
        val bookingTimestampTextView: TextView = itemView.findViewById(R.id.bookingTimestampTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val note: TextView = itemView.findViewById(R.id.noteTextView)
        val declineButton: Button = itemView.findViewById(R.id.declineButton)
        val completeButton: Button = itemView.findViewById(R.id.completeButton)
        val cancelButton: Button = itemView.findViewById(R.id.cancelButton)
    }
}