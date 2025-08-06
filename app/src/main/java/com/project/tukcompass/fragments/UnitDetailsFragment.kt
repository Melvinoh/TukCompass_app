package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentUnitDetailsBinding


class UnitDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUnitDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       binding = FragmentUnitDetailsBinding.inflate(
           inflater,
           container,
           false
       )
        return binding.root
    }

}