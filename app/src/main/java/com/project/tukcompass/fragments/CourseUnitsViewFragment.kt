package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.CourseSemUnitAdapter
import com.project.tukcompass.databinding.FragmentCourseUnitsViewBinding
import com.project.tukcompass.models.YearTab


class CourseUnitsViewFragment : Fragment() {
    private lateinit var binding: FragmentCourseUnitsViewBinding
    private lateinit var yearTab: YearTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yearTab = requireArguments().getParcelable("yearTab") ?: YearTab(0, emptyList())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCourseUnitsViewBinding .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CourseSemUnitAdapter(yearTab.semesters)

        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(yearTab: YearTab): CourseUnitsViewFragment {
            val fragment = CourseUnitsViewFragment  ()
            fragment.arguments = bundleOf("yearTab" to yearTab)
            return fragment
        }
    }
}

