package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.R
import com.project.tukcompass.databinding.ViewholderCourseUnitsBinding
import com.project.tukcompass.databinding.ViewholderSemUnitsBinding
import com.project.tukcompass.models.EventModel

import com.project.tukcompass.models.SemesterTab

class CourseSemUnitAdapter(
    private val semesters: List<SemesterTab>

) : RecyclerView.Adapter<CourseSemUnitAdapter.SemesterViewHolder>() {

    inner class SemesterViewHolder(val binding: ViewholderSemUnitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SemesterViewHolder {
        val binding = ViewholderSemUnitsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SemesterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SemesterViewHolder, position: Int) {
        val sem = semesters[position]
        holder.binding.semesterTitle.text = "Semester ${sem.sem}"
        holder.binding.unitRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                holder.itemView.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = CourseUnitAdapter(sem.units){ unit ->
                val bundle = bundleOf("unit" to unit)
                findNavController().navigate(R.id.unitContentFragment, bundle)

            }
        }
    }
    override fun getItemCount() = semesters.size
}