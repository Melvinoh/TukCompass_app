package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.ViewholderCourseUnitsBinding
import com.project.tukcompass.models.UnitItem

class CourseUnitAdapter(
    private val units: List<UnitItem>,
    private val onItemClick: (UnitItem) -> Unit
) : RecyclerView.Adapter<CourseUnitAdapter.UnitViewHolder>() {

    inner class UnitViewHolder(val binding: ViewholderCourseUnitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val binding = ViewholderCourseUnitsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val unit = units[position]
        holder.binding.nameTxt.text = unit.unitName ?: "N/A"
        holder.binding.codeTxt.text = unit.unitID
        holder.itemView.setOnClickListener {
            onItemClick(unit)
        }
    }
    override fun getItemCount() = units.size
}
