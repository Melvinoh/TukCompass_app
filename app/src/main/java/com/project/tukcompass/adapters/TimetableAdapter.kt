package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.ViewholderTimeslotBinding

import com.project.tukcompass.databinding.ViewholderTimtableCellBinding
import com.project.tukcompass.models.SessionTable

class TimetableAdapter(
    private val sessions: Map<String, SessionTable?>
) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderTimtableCellBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderTimtableCellBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        val session = sessions[day]

        // Safely set values
        holder.binding.unitName.text = session?.unitName ?: ""
        holder.binding.modeTxt.text = session?.mode ?: ""
    }

    override fun getItemCount(): Int = days.size
}
