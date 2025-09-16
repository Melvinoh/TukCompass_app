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
import com.project.tukcompass.adapters.AnnouncementAdapter
import com.project.tukcompass.adapters.LecturerAdapter
import com.project.tukcompass.adapters.PastPapersAdapter
import com.project.tukcompass.databinding.FragmentUnitContentBinding
import com.project.tukcompass.models.SessionDisplayItem
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AcademicsViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class UnitContentFragment : Fragment() {

    private lateinit var  binding: FragmentUnitContentBinding
    private val viewModel: AcademicsViewHolder by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnitContentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val unitID = "ECSI4203"

        viewModel.getUnitDetails(unitID)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        observeUnitInfo()
        observeLecturers()
        observePastPapers()

    }

    private fun observePastPapers(){
        viewModel.unitDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val pastPapers = response.data?.pastPapers ?: emptyList()
                    Log.d("announcementLog", "${pastPapers}")
                    binding.papersRv.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val adapter = binding.papersRv.adapter as? PastPapersAdapter
                    if (adapter == null) {
                        binding.papersRv.adapter = PastPapersAdapter(pastPapers) {  pastPaper ->

                            val fragment = AnnouncementDetailsFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable("item",pastPaper)
                                }
                            }
                            fragment.show(childFragmentManager, "AnnouncementDetailsFragment")
                        }
                    } else {
                        adapter.updateAdapter(pastPapers)
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

    private fun observeLecturers(){
        viewModel.unitDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val lecturers = response.data.lecturers

                    Log.d("announcementLog", "$lecturers")
                    binding.lecRv.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val adapter = binding.lecRv.adapter as? LecturerAdapter

                    if (adapter == null) {
                        binding.lecRv.adapter = LecturerAdapter(lecturers) { pastPaper ->
                            findNavController().navigate(R.id.unitEnrolmentFragment)
                        }
                    } else {
                        adapter.updateAdapter(lecturers)
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
    private fun observeUnitInfo(){
        viewModel.unitDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val unitInfo = response.data.unit
                    Log.d("unitInfo log", "${unitInfo}")

                    binding.unitName.text = unitInfo.unitName
                    binding.unitCode.text = unitInfo.unitID

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