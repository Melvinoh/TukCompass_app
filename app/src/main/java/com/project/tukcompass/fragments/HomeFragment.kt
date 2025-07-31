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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.AnnouncementAdapter
import com.project.tukcompass.adapters.CategoryAdapter
import com.project.tukcompass.adapters.EventsAdapter
import com.project.tukcompass.adapters.MyGroupAdapter
import com.project.tukcompass.databinding.FragmentHomeBinding
import com.project.tukcompass.models.CategoryModel
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager

    val categoryList = listOf(
        CategoryModel("Syllabus", R.drawable.curriculum),
        CategoryModel("past papers", R.drawable.test),
        CategoryModel("Clubs", R.drawable.clubs),
        CategoryModel("Academics", R.drawable.sylabus),
        CategoryModel("My connects", R.drawable.network),
        CategoryModel("My units", R.drawable.winner)
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
        sharedPrefManager = EncryptedSharedPrefManager(requireContext())

        val user = sharedPrefManager.getUser()
        val token = sharedPrefManager.getToken()

        binding.userName.text = user?.fname

        Log.d("userLog", "${user}")
        Log.d("tokenLog", "${token}")

        binding.allEvents.setOnClickListener {

            findNavController().navigate(R.id.eventsFragment,)

        }


        observeCategories()

        viewModel.getEvents()
        observeEvents()

        viewModel.getAnnouncement()
        observeAnnouncements()

        viewModel.getMyClubs()
        observeMyClubs()

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
                            val bundle = bundleOf("event" to event)
                            findNavController().navigate(R.id.eventsDetailsFragment,bundle)
                        }
                    } else {
                        adapter.updateEvents(events) // Assuming EventsAdapter has an update method
                    }
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

                            val fragment = AnnouncementDetailsFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable("item", announcement)
                                }
                            }
                            fragment.show(childFragmentManager, "AnnouncementDetailsFragment")
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
    private fun observeMyClubs(){
        viewModel.clubSports.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val clubSports = response.data?.clubSports ?: emptyList()
                    Log.d("clubLog", "${clubSports}")



                    binding.viewGroups.layoutManager = GridLayoutManager(
                        requireContext(),
                        2
                    )

                    val adapter = binding.viewGroups.adapter as? MyGroupAdapter

                    if (adapter == null) {
                        binding.viewGroups.adapter = MyGroupAdapter(clubSports) { clubSport ->
                            val bundle = bundleOf("club" to clubSport)
                            findNavController().navigate(R.id.clubsFragment,bundle)
                        }
                    } else {
                        adapter.updateGroups(clubSports)
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
    private fun displayFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)//this container is in main activity
            .addToBackStack("null")
            .commit()
    }

}