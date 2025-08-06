package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.VieholderClubSportsBinding
import com.project.tukcompass.models.ClubSportModel

class MyClubSportAdapter(
    private var myClubSport : List<ClubSportModel>,
    private val onItemClick: (ClubSportModel) -> Unit
)
    : RecyclerView.Adapter<MyClubSportAdapter.ViewHolder>() {

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
        val myClubSport = myClubSport[position]

            holder.binding.titleTxt.text = myClubSport.name
            Glide.with(holder.itemView.context)
                .load(myClubSport.profileURL)
                .into(holder.binding.imgView)

            holder.itemView.setOnClickListener {
                onItemClick(myClubSport)
            }

    }
    override fun getItemCount(): Int = myClubSport.size

    fun updateGroups(newGroups: List<ClubSportModel>) {
        myClubSport = newGroups
        notifyDataSetChanged()
    }
}