package com.project.tukcompass.fragments

import android.app.TimePickerDialog

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.google.android.material.datepicker.MaterialDatePicker
import com.project.tukcompass.R

import com.project.tukcompass.databinding.FragmentEventRegistrationBinding
import com.project.tukcompass.models.EventRequest

import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EventRegistrationFragment : Fragment() {
    //
    private lateinit var binding: FragmentEventRegistrationBinding
    private val viewModel: HomeViewModel by viewModels()
    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.imagePreview.setImageURI(it)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventRegistrationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTxt.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build()

            datePicker.addOnPositiveButtonClickListener { millis ->
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                binding.dateTxt.setText(date)
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
        binding.timeTxt.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    binding.timeTxt.setText(String.format("%02d:%02d", hourOfDay, minute))
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }
        binding.uploadFile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.submitBtn.setOnClickListener {

            val title = binding.titleTxt.text.toString()
            val desc = binding.descriptionTxt.text.toString()
            val loc = binding.locationTxt.text.toString()
            val date = binding.dateTxt.text.toString()
            val time = binding.timeTxt.text.toString()

            if (title.isEmpty() || desc.isEmpty() || loc.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val event = EventRequest(
                title = title,
                description = desc,
                location = loc,
                date = date,
                time = binding.timeTxt.text.toString()
            )
            val message :String = "Event created successfully!"
            val destination :Int = R.id.eventsFragment

            viewModel.addEvent(event, imageUri, requireContext())
            Log.d("EventLog", "${event}")
            observeEvents(message, destination)

          findNavController().navigate(R.id.eventsFragment)
        }
    }

    private fun observeEvents( msg :String, dest :Int) {
        viewModel.events.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    SuccessDialogFragment.newInstance(
                        "You have successfully enrolled ",
                        R.id.eventsFragment,
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