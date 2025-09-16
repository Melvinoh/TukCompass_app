package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.project.tukcompass.R
import com.project.tukcompass.adapters.CourseSemUnitAdapter
import com.project.tukcompass.adapters.CourseUnitViewPager
import com.project.tukcompass.adapters.EventsAdapter
import com.project.tukcompass.databinding.FragmentCourseUnitsBinding
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AcademicsViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseUnitsFragment : Fragment() {

    private val viewModel: AcademicsViewHolder by viewModels()
    private lateinit var binding: FragmentCourseUnitsBinding
    private lateinit var pagerAdapter: CourseUnitViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseUnitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = CourseUnitViewPager(this)
        binding.viewPager.adapter = pagerAdapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val yearTab = pagerAdapter.getYearTab(position)
            tab.text = "Year ${yearTab.year}"
        }.attach()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.courseUnits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val grouped = response.data?.grouped ?: emptyList()
                    Log.d("grouped", "${grouped}")
                    pagerAdapter.setData(grouped)

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    Log.d("error", "$response.message")

                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    Log.d("loading", "Loading")
                }
                else -> {}
            }
        }

        viewModel.fetchCourseUnits()
    }
}
