package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.EventsViewholderBinding
import com.project.tukcompass.databinding.VieholderEventsBinding
import com.project.tukcompass.models.EventModel

class EventAdapterList (

    private var events: List<EventModel>,
    private val onItemClick: (EventModel) -> Unit

) : RecyclerView.Adapter<EventAdapterList.ViewHolder>() {


    inner class ViewHolder(val binding: EventsViewholderBinding ) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapterList.ViewHolder {
        val binding = EventsViewholderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventAdapterList.ViewHolder, position: Int) {

        val event = events[position]
        holder.binding.titleTxt.text = event.title
        holder.binding.dateTxt.text = event.date
        holder.binding.locationTxt.text = event.location
        holder.binding.dateTxt.text = event.time
        Glide.with(holder.itemView.context)
            .load(event.fileUrl)
            .into(holder.binding.profilePic)

        holder.itemView.setOnClickListener {
            onItemClick(event)
        }

    }

    override fun getItemCount(): Int =events.size

    fun updateEvents(newEvents: List<EventModel>) {
        events = newEvents
        notifyDataSetChanged()
    }


}