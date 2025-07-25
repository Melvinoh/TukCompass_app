package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.VieholderClubSportsBinding
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.ClubSportModel

class MyGroupAdapter(

    private  var  groups: List<ClubSportModel>,
    private val onItemClick: (ClubSportModel) -> Unit

): RecyclerView.Adapter<MyGroupAdapter.ViewHolder>() {

    inner  class ViewHolder(val binding: VieholderClubSportsBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = VieholderClubSportsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val group = groups[position]
        holder.binding.titleTxt.text = group.name
        Glide.with(holder.itemView.context)
            .load(group.profileURL)
            .into(holder.binding.imgView)

        holder.itemView.setOnClickListener {
            onItemClick(group)
        }
    }

    override fun getItemCount(): Int = groups.size

    fun updateGroups(newGroups: List<ClubSportModel>) {
        groups = newGroups
        notifyDataSetChanged()
    }

}