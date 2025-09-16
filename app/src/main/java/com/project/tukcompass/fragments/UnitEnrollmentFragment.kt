package com.project.tukcompass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentUnitEnrollmentBinding
import com.project.tukcompass.models.SessionDisplayItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnitEnrollmentFragment : Fragment() {

    private  lateinit var binding: FragmentUnitEnrollmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnitEnrollmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.submitBtn.setOnClickListener {

            findNavController().navigate(R.id.unitDetailsFragment)
        }
    }

}