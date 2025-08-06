package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.ViewholderTimeslotBinding

import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.utills.GridItemDecoration

class TimeSlotAdapter(
    private var slots: List<TimeSlots>
) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderTimeslotBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeSlotAdapter.ViewHolder {
        val binding = ViewholderTimeslotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TimeSlotAdapter.ViewHolder,
        position: Int
    ) {
        val decoration = GridItemDecoration(6)

        val slot = slots[position]
        holder.binding.time.text = slot.key
        holder.binding.tsRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.binding.tsRecyclerView.addItemDecoration(decoration)
        holder.binding.tsRecyclerView.adapter = TimetableAdapter(slot.sessions)
    }
    override fun getItemCount(): Int = slots.size


    fun updateTimeSlots(newTimeSlots: List<TimeSlots>) {
        slots = newTimeSlots
        notifyDataSetChanged()
    }
}
