package com.project.tukcompass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.ViewholderAnnoncmentBinding
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.PastPaper

class PastPapersAdapter(
    private var pastPapers: List<PastPaper>,
    private val onItemClick: (PastPaper) -> Unit
) : RecyclerView.Adapter<PastPapersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderAnnoncmentBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastPapersAdapter.ViewHolder {

        val binding =ViewholderAnnoncmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PastPapersAdapter.ViewHolder, position: Int) {

        val pastPaper = pastPapers[position]
        Log.d("adapter announcementLog", "${pastPaper}")

        val isPdf = pastPaper.PdfUrl.endsWith(".pdf")
        val displayUrl = if (isPdf) {

            val baseUrl = pastPaper.PdfUrl.substringBeforeLast("/v")
            val versionAndPath = pastPaper.PdfUrl.substringAfterLast("/v")
            "$baseUrl/fl_attachment,pg_1,f_jpg/v$versionAndPath"

        }else{
            pastPaper.PdfUrl

        }
        Glide.with(holder.itemView.context)
            .load(displayUrl)
            .into(holder.binding.announcementImage)

        holder.itemView.setOnClickListener {
            onItemClick(pastPapers[position])
        }
    }

    override fun getItemCount(): Int = pastPapers.size

    fun updateAdapter(newAnnouncements: List<PastPaper>) {
        pastPapers = newAnnouncements
        notifyDataSetChanged()
    }
}