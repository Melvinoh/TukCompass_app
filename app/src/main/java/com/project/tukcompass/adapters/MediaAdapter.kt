package com.project.tukcompass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.ViewholderMediaBinding
import com.project.tukcompass.models.ContentItem

class MediaAdapter(
    private var media: List<ContentItem>,
    private val onItemClick: (ContentItem) -> Unit
) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderMediaBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaAdapter.ViewHolder {

        val binding = ViewholderMediaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaAdapter.ViewHolder, position: Int) {

        val medium = media[position]
        Log.d("adapter announcementLog", "${medium}")
        holder.binding.titleTxt

        val isPdf = medium.url.endsWith(".pdf")
        val displayUrl = if (isPdf) {

            val baseUrl = medium.url.substringBeforeLast("/v")
            val versionAndPath = medium.url.substringAfterLast("/v")
            "$baseUrl/fl_attachment,pg_1,f_jpg/v$versionAndPath"

        }else{
            medium.url

        }
        Glide.with(holder.itemView.context)
            .load(displayUrl)
            .into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(media[position])
        }
    }

    override fun getItemCount(): Int = media.size

    fun updateAdapter(newMedia: List<ContentItem>) {
        media = newMedia
        notifyDataSetChanged()
    }
}