package com.project.tukcompass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.ViewholderAnnoncmentBinding
import com.project.tukcompass.models.AnnouncementModel

class AnnouncementAdapter(
    private var announcements: List<AnnouncementModel>,
    private val onItemClick: (AnnouncementModel) -> Unit

) : RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderAnnoncmentBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementAdapter.ViewHolder {

        val binding =ViewholderAnnoncmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementAdapter.ViewHolder, position: Int) {

        val announcement = announcements[position]
        Log.d("adapter announcementLog", "${announcement}")
        Glide.with(holder.itemView.context)
            .load(announcement.fileUrl)
            .into(holder.binding.announcementImage)

        holder.itemView.setOnClickListener {
            onItemClick(announcements[position])
        }


    }

    override fun getItemCount(): Int = announcements.size

    fun updateAnnouncement(newAnnouncements: List<AnnouncementModel>) {
        announcements = newAnnouncements
        notifyDataSetChanged()
    }
}