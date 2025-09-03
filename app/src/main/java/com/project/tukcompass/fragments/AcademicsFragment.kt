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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.MyGroupAdapter
import com.project.tukcompass.adapters.TimeSlotAdapter
import com.project.tukcompass.adapters.UpComingClassAdapter
import com.project.tukcompass.databinding.FragmentAcademicsBinding
import com.project.tukcompass.databinding.FragmentHomeBinding
import com.project.tukcompass.models.SessionDisplayItem
import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.models.TimetableResponse
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AcademicsViewHolder
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import kotlin.collections.emptyList
import kotlin.getValue

@AndroidEntryPoint
class AcademicsFragment : Fragment() {

    private val viewModel: AcademicsViewHolder by viewModels()
    private lateinit var binding: FragmentAcademicsBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcademicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStudentsTimetable()
        observeUpcomingClass()
        observeTimetable()
    }

    private fun observeTimetable(){
        viewModel.timetable.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val timetable = response.data.timetable ?: emptyList()
                    Log.d("timetable", "$timetable")

                    binding.rvTimeslots.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )

                    val adapter = binding.rvTimeslots.adapter as? TimeSlotAdapter

                    if (adapter == null) {
                        binding.rvTimeslots.adapter = TimeSlotAdapter(timetable)
                    } else {
                        adapter.updateTimeSlots(timetable)
                    }
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
    private fun observeUpcomingClass(){
        viewModel.timetable.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val timetable = response.data.timetable ?: emptyList()
                    Log.d("timetable log ", "$timetable")
                    val currentDay = getCurrentDay()
                    val todaySessions = timetable.mapNotNull { slot ->
                        val session = slot.sessions[currentDay]
                        session?.let {
                            SessionDisplayItem(
                                unitName = it.unitName ?: "",
                                unitID = it.unitID ?: "",
                                unitOfferingID = it.unitOfferingID ?: "",
                                lecturerName = it.lecturerName ?: "",
                                mode = it.mode ?: "",
                                startTime = slot.startTime,
                                endTime = slot.endTime
                            )
                        }
                    }
                    if(!todaySession){
Toast.make(requireContext(), "you do not have classes today enjoy", Toast.LENGTH_SHORT).show()

                    }



                    binding.upcomingViewholder.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    val adapter = binding.upcomingViewholder as? UpComingClassAdapter

                    if (adapter == null) {
                        binding.upcomingViewholder.adapter = UpComingClassAdapter(todaySessions){ unitDetails ->
                            val bundle = bundleOf("unitDetails" to unitDetails)
                            findNavController().navigate(R.id.unitDetailsFragment,bundle)
                        }
                    } else {
                        adapter.updateSessions(todaySessions)
                    }
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

    fun getCurrentDay(): String {
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
    }


}
