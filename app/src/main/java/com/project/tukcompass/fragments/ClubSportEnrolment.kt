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
import com.project.tukcompass.R
import com.project.tukcompass.adapters.EventsAdapter
import com.project.tukcompass.databinding.FragmentClubSportEnrolmentBinding
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportReq
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ClubSportEnrolment : Fragment() {

    private lateinit var binding: FragmentClubSportEnrolmentBinding
    private lateinit var club: ClubSportModel
    private lateinit  var clubId: String

    private val viewModel: ClubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClubSportEnrolmentBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        club = requireArguments().getParcelable("club") ?: ClubSportModel()
        clubId = club.clubSportsID

        val clubSportID = ClubSportReq(clubId)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.submitBtn.setOnClickListener {
            viewModel.enrollClubSport(clubSportID)

            viewModel.enrollmentStatus.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        SuccessDialogFragment.newInstance(
                            "You have successfully enrolled to ${club.name}!",
                            R.id.clubsFragment,
                            club = club
                        ).show(parentFragmentManager, "SuccessDialog")
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}