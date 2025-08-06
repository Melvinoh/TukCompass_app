package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.ViewholderTimtableCellBinding
import com.project.tukcompass.databinding.ViewholderUnitsBinding
import com.project.tukcompass.models.SessionDisplayItem
import com.project.tukcompass.models.SessionTable

class UpComingClassAdapter(
    private var sessions: List<SessionDisplayItem>
) : RecyclerView.Adapter<UpComingClassAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderUnitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderUnitsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val session = sessions[position]

        // Safely set values
        holder.binding.unitName.text = session?.unitName ?: ""
        holder.binding.locationTxt.text = session?.mode ?: ""
        holder.binding.timeTxt.text = session?.lecturerName ?: ""
    }

    override fun getItemCount(): Int = sessions.size

    fun updateSessions(newSessions: List<SessionDisplayItem>) {
        this.sessions = newSessions
        notifyDataSetChanged()
    }

}