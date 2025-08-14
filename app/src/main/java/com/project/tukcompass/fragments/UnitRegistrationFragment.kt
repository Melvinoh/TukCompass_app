package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.ScheduleAdapter
import com.project.tukcompass.adapters.TimeSlotAdapter
import com.project.tukcompass.databinding.FragmentUnitRegistrationBinding
import com.project.tukcompass.models.CourseRequest
import com.project.tukcompass.models.ScheduleBlock
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AcademicsViewHolder
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
@AndroidEntryPoint
class UnitRegistrationFragment : Fragment() {

    private lateinit var binding: FragmentUnitRegistrationBinding
    private val viewModel: AcademicsViewHolder by viewModels()
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnitRegistrationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val schedules = mutableListOf(ScheduleBlock())

        scheduleAdapter= ScheduleAdapter(schedules){ courseID, position ->
            fetchCourse(courseID,position)
        }
        binding.addSchedule.setOnClickListener {
            schedules.add(ScheduleBlock())
            scheduleAdapter.notifyItemInserted(schedules.size - 1)
        }

    }
    private fun fetchCourse(courseID: String, position: Int){
        val reqBody = CourseRequest(courseID)

        viewModel.fetchCourses(reqBody)

        viewModel.courses.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val courses = response.data.courseName  ?: emptyList()
                    scheduleAdapter.updateCourseSuggestions(position,courses)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    Log.d("error", "${response.message}")
                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    Log.d("loading", "Loading")
                }
                else -> {}
            }
        }
    }
}


