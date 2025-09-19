package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.project.tukcompass.R
import com.project.tukcompass.adapters.ViewPagerAdapter
import com.project.tukcompass.databinding.FragmentUnitDetailsBinding
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.SessionDisplayItem
import com.project.tukcompass.viewModels.AcademicsViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class UnitDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUnitDetailsBinding

    private lateinit var  unitDetails: SessionDisplayItem

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        unitDetails = arguments?.getParcelable<SessionDisplayItem>("unitDetails") ?: SessionDisplayItem()



        binding.unitName.text = unitDetails.unitName
        binding.lecUnitName.text = unitDetails.unitName
        binding.lecName.text = unitDetails.lecturerName

        Glide.with(requireContext())
            .load(unitDetails.lecturerProfile)
            .placeholder(R.drawable.ic_account)
            .into(binding.profilePic)


        val titles = listOf("Notes", "Assigno", "Media", "Links")


        val fragments = listOf(
            UnitContentViewFragment.newInstance("pdf", unitDetails ),
            UnitContentViewFragment.newInstance("assignment", unitDetails),
            UnitContentViewFragment.newInstance("video", unitDetails),
            UnitContentViewFragment.newInstance("link",unitDetails)
        )

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        val adapter = ViewPagerAdapter(requireActivity(), fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = layoutInflater.inflate(R.layout.tab_layout, null)
            val title = tabView.findViewById<TextView>(R.id.tabTxt)
            title.text = titles[position]
            tab.customView = tabView

        }.attach()
    }

}