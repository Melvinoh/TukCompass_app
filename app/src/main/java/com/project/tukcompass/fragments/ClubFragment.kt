package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentClubBinding
class ClubFragment : Fragment() {
    private lateinit var  binding: FragmentClubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClubBinding.inflate(inflater, container, false)

        return binding.root
    }

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarLayout = binding.appBarLayout
        val toolbar = binding.toolbar
        val headerImage = binding.headerImage
        val avatarImage = binding.avatarImage
        val titleText = binding.titleText

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            val scrollRange = appBarLayout.totalScrollRange
            val scrollPercentage = Math.abs(verticalOffset / scrollRange.toFloat())
            val avatarScale = 1 - (0.5f * scrollPercentage)

            avatarImage.scaleX = avatarScale
            avatarImage.scaleY = avatarScale
            avatarImage.translationY - 80 * scrollPercentage

            titleText.translationY = -90 * scrollPercentage
            titleText.translationX = -30 * scrollPercentage

            headerImage.alpha = 1 - scrollPercentage

        }



    }

}