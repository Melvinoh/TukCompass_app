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
import com.project.tukcompass.adapters.MyClubSportAdapter
import com.project.tukcompass.adapters.MyGroupAdapter
import com.project.tukcompass.databinding.FragmentAllClubSportBinding
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ClubViewModel
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class AllClubSportFragment : Fragment() {

    private val viewModel: ClubViewModel by viewModels()
    private lateinit var binding: FragmentAllClubSportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllClubSportBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMyClubs()
        observeMyClubSport()

        viewModel.getClubSport()
        observeClubsSport()

    }
    private fun observeMyClubSport() {
        viewModel.myClubSports.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val myClubSports = response.data?.clubSports ?: emptyList()

                    val clubs = myClubSports.filter { it.type == "clubs" }
                    val sports = myClubSports.filter { it.type == "sports" }

                    // Setup clubs RecyclerView
                    binding.clubRv.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val currentClubsAdapter = binding.clubRv.adapter as? MyClubSportAdapter
                    if (currentClubsAdapter == null) {
                        binding.clubRv.adapter = MyClubSportAdapter(clubs) { clubSport ->
                            val bundle = bundleOf("club" to clubSport)
                            findNavController().navigate(R.id.clubsFragment, bundle)
                        }
                    } else {
                        currentClubsAdapter.updateGroups(clubs)
                    }

                    // Setup sports RecyclerView
                    binding.sportsRv.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val currentSportsAdapter = binding.sportsRv.adapter as? MyClubSportAdapter
                    if (currentSportsAdapter == null) {
                        binding.sportsRv.adapter = MyClubSportAdapter(sports) { clubSport ->
                            val bundle = bundleOf("club" to clubSport)
                            findNavController().navigate(R.id.clubsFragment, bundle)
                        }
                    } else {
                        currentSportsAdapter.updateGroups(sports)
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    Log.e("ClubSportError", "${response.message}")
                }

                is Resource.Loading -> {
                    Log.d("ClubSportLoading", "Loading...")
                }

                else -> Unit
            }
        }
    }
    private fun observeClubsSport(){
        viewModel.clubSports.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val clubSports = response.data?.clubSports ?: emptyList()
                    Log.d("clubLog", "${clubSports}")

                    binding.allClubSportRv.layoutManager = GridLayoutManager(
                        requireContext(),
                        3
                    )
                    val adapter = binding.allClubSportRv.adapter as? MyGroupAdapter

                    if (adapter == null) {
                        binding.allClubSportRv.adapter = MyGroupAdapter(clubSports) { clubSport ->
                            val bundle = bundleOf("club" to clubSport)
                            findNavController().navigate(R.id.clubSportEnrollment,bundle)
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
}

