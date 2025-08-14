package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.ViewholderLecturersBinding
import com.project.tukcompass.models.Lecturer
import com.project.tukcompass.models.PastPaper

class LecturerAdapter(
    private var lecturers: List<Lecturer>,
    private val onEnrollClick: (Lecturer) -> Unit
) : RecyclerView.Adapter<LecturerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderLecturersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderLecturersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = lecturers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lecturers[position]
        with(holder.binding) {
            nameTxt.text = item.fullName
            academicTxt.text = item.academicYear
            enrollBtn.setOnClickListener { onEnrollClick(item) }
        }
    }
    fun updateAdapter(newAnnouncements: List<Lecturer>) {
        lecturers = newAnnouncements
        notifyDataSetChanged()
    }
}