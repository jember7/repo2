package com.capstone.homeease.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.homeease.R
import com.capstone.homeease.adapters.ExpertsAdapter
import com.capstone.homeease.controller.ExpertController
import com.capstone.homeease.model.Expert

class AvailableExpertsFragment : Fragment(), ExpertView {

    private lateinit var expertsRecyclerView: RecyclerView
    private lateinit var expertsAdapter: ExpertsAdapter
    private var experts: MutableList<Expert> = mutableListOf()
    private lateinit var expertController: ExpertController

    companion object {
        private const val ARG_PROFESSION = "profession"

        fun newInstance(profession: String): AvailableExpertsFragment {
            val fragment = AvailableExpertsFragment()
            val args = Bundle()
            args.putString(ARG_PROFESSION, profession) // Use the correct key here
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_available_experts, container, false)

        expertsRecyclerView = binding.findViewById(R.id.expertsRecyclerView)
        expertsRecyclerView.layoutManager = LinearLayoutManager(context)
        expertsAdapter = ExpertsAdapter(requireContext(), experts)
        expertsRecyclerView.adapter = expertsAdapter

        expertController = ExpertController(this)

        val profession = arguments?.getString(ARG_PROFESSION) ?: ""
        val professionTitle = binding.findViewById<TextView>(R.id.title)
        professionTitle.text = profession

        expertController.fetchExperts(profession)

        return binding
    }

    override fun showExperts(expertsList: List<Expert>) {
        expertsAdapter.updateExperts(expertsList)
    }

    override fun showError(errorMessage: String) {
        // Handle error, show toast or UI feedback
    }
}