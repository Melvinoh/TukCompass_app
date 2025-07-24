package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.AnnouncementAdapter
import com.project.tukcompass.adapters.CategoryAdapter
import com.project.tukcompass.adapters.EventsAdapter
import com.project.tukcompass.databinding.FragmentHomeBinding
import com.project.tukcompass.models.CategoryModel
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    val categoryList = listOf(
        CategoryModel("Syllabus", R.drawable.microphone),
        CategoryModel("past papers", R.drawable.notification),
        CategoryModel("Clubs", R.drawable.favourite_outlined),
        CategoryModel("Academics", R.drawable.share_outlined),
        CategoryModel("My connects", R.drawable.calendar_outlined),
        CategoryModel("My units", R.drawable.chats_outlined)
    )

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


        observeCategories()

        viewModel.getEvents()
        observeEvents()

        viewModel.getAnnouncement()
        observeAnnouncements()

        binding.viewEvents.setOnClickListener {
            val fragment = EventsFragment()
            displayFragment(fragment)
        }

    }


    private fun observeCategories() {

        binding.viewCategory.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.viewCategory.adapter = CategoryAdapter(categoryList) { category ->

            when (category.title) {
                "Syllabus" -> displayFragment(AcademicsFragment())
                "past papers" -> displayFragment(AcademicsFragment())
                "Clubs" -> displayFragment(ClubFragment())
                "Academics" -> displayFragment(AcademicsFragment())
                "My connects" -> displayFragment(ChatFragment())
                "My units" -> displayFragment(AccountFragment())
                else -> Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun observeEvents() {
        viewModel.events.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val events = response.data?.events ?: emptyList()
                    Log.d("EventLog", "${events}")

                    binding.viewEvents.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val adapter = binding.viewEvents.adapter as? EventsAdapter
                    if (adapter == null) {
                        binding.viewEvents.adapter = EventsAdapter(events) { event ->
                            val fragment = EventDetailsFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable("event", event)
                                }
                            }
                            displayFragment(fragment)
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
    private fun observeAnnouncements(){
        viewModel.announcements.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val announcements = response.data?.announcements ?: emptyList()
                    Log.d("announcementLog", "${announcements}")
                    binding.viewAnnouncement.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val adapter = binding.viewAnnouncement.adapter as? AnnouncementAdapter
                    if (adapter == null) {
                        binding.viewAnnouncement.adapter = AnnouncementAdapter(announcements) { announcement ->

                            val fragment = EventDetailsFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable("item", announcement)
                                }
                            }
                            displayFragment(fragment)

                        }
                    } else {
                        adapter.updateAnnouncement(announcements)
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
    private fun observeClubSports(){
        viewModel.clubSports.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                else -> {}
            }
        }
    }
    private fun displayFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)//this container is in main activity
            .addToBackStack("null")
            .commit()
    }

}