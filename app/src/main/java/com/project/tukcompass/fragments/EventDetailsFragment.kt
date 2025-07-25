package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentEventDetailsBinding
import com.project.tukcompass.models.EventModel


class EventDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding
    private lateinit var event: EventModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event = arguments?.getParcelable("event")!!

        binding.titleTxt.text = event.title
        binding.locationTxt.text = event.location
        binding.timeTxt.text = event.time
        binding.dateTxt.text = event.date
        binding.descriptionTxt.text = event.description

        Glide.with(requireContext())
            .load(event.fileUrl)
            .into(binding.imgView)

    }


}