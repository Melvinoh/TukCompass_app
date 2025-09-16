package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.EventAdapterList
import com.project.tukcompass.adapters.EventsAdapter
import com.project.tukcompass.databinding.FragmentEventsBinding
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private lateinit var binding: FragmentEventsBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getEvents()
        observeUpcomingEvents()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun observeUpcomingEvents() {
        viewModel.events.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val events = response.data?.events ?: emptyList()

                    Log.d("EventLog", "${events}")

                    binding.upcomingEventHolder.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    val adapter = binding.upcomingEventHolder.adapter as? EventAdapterList
                    if (adapter == null) {
                        binding.upcomingEventHolder.adapter = EventAdapterList(events) { event ->
                            val bundle = bundleOf("event" to event)
                            findNavController().navigate(R.id.eventsDetailsFragment,bundle)
                        }
                    } else {
                        adapter.updateEvents(events)
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
    private fun observerPastEvents() {
        viewModel.events.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val events = response.data?.events ?: emptyList()
                    Log.d("EventLog", "${events}")

                    binding.pastEventsHolder.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val adapter = binding.pastEventsHolder.adapter as? EventsAdapter
                    if (adapter == null) {
                        binding.pastEventsHolder.adapter = EventsAdapter(events) { event ->
                            val fragment = EventDetailsFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable("event", event)
                                }
                            }
                            findNavController().navigate(R.id.eventsFragment)
                        }
                    } else {
                        adapter.updateEvents(events) // Assuming EventsAdapter has an update method
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
}