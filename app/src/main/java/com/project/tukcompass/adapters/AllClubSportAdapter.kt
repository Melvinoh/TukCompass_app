package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.VieholderClubSportsBinding
import com.project.tukcompass.models.ClubSportModel

class AllClubSportAdapter(

    private var cluSport : List<ClubSportModel>,
    private val onItemClick: (ClubSportModel) -> Unit

): RecyclerView.Adapter<AllClubSportAdapter.ViewHolder>(){

    inner class ViewHolder( val binding: VieholderClubSportsBinding)
        : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllClubSportAdapter.ViewHolder {
        val binding = VieholderClubSportsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllClubSportAdapter.ViewHolder, position: Int) {

        val clubSports = cluSport[position]

        holder.binding.titleTxt.text = clubSports.name
        Glide.with(holder.itemView.context)
            .load(clubSports.profileURL)
            .into(holder.binding.imgView)

        holder.itemView.setOnClickListener {
            onItemClick(clubSports)
        }

    }

    override fun getItemCount(): Int = cluSport.size

    fun updateGroups(newGroups: List<ClubSportModel>) {
         cluSport= newGroups
        notifyDataSetChanged()
    }
}