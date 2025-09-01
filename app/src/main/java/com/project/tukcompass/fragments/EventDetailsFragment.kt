package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentEventDetailsBinding
import com.project.tukcompass.models.EventModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        event = arguments?.getParcelable("event") ?: EventModel()

        event?.let{
            
            binding.titleTxt.text = event.title
            binding.locationTxt.text = event.location
            binding.timeTxt.text = event.time
            binding.dateTxt.text = event.date
            binding.descriptionTxt.text = event.description

        } :? run {
            Toast.make(requireContext(),"Event not Found",Toast.LENGTH_SHORT).show()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        Glide.with(requireContext())
            .load(event.fileUrl)
            .into(binding.imgView)

    }

    companion object {
        fun newInstance(event: EventModel) = EventDetailsFragment().apply{
            arguments = bundleOf("event" to event)
        }
    }


}
