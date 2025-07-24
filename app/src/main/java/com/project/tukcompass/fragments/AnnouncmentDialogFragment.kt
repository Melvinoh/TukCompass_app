package com.project.tukcompass.fragments

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.AnnouncmentModalBinding
import com.project.tukcompass.models.AnnouncementModel

class AnnouncmentDialogFragment : DialogFragment() {

    private var _binding: AnnouncmentModalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AnnouncmentModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item: ArrayList<AnnouncementModel>
        item = arguments?.getParcelableArrayList<AnnouncementModel>("item") as ArrayList<AnnouncementModel>


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }





}