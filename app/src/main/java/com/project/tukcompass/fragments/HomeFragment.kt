package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.tukcompass.databinding.FragmentHomeBinding
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.HomeViewModel


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun observeEvents() {
        viewModel.events.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.success -> {






                }
                is Resource.error -> {

                }
                is Resource.loading -> {

                }
                else -> {}
            }
        }
    }
    private fun observeAnnouncements(){
        viewModel.announcements.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.success -> {

                }
                is Resource.error -> {

                }
                is Resource.loading -> {

                }
                else -> {}
            }
        }
    }
    private fun observeClubSports(){
        viewModel.clubSports.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.success -> {

                }
                is Resource.error -> {

                }
                is Resource.loading -> {

                }
                else -> {}
            }
        }
    }

}