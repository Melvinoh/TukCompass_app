package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.FragmentUnitRegistrationBinding
import com.project.tukcompass.databinding.VieholderScheduleItemBinding
import com.project.tukcompass.models.TimeSchedule
import java.sql.Time

class ScheduleSlotAdapter(

    private var itemSchedule: MutableList<TimeSchedule>

) : RecyclerView.Adapter<ScheduleSlotAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: VieholderScheduleItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun getUpdatedSlots(): List<TimeSchedule> = itemSchedule
        }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleSlotAdapter.ViewHolder {
        val binding = VieholderScheduleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ScheduleSlotAdapter.ViewHolder, position: Int) {

        val scheduleSlot = itemSchedule[position]

        val days = listOf<String>("Monday","Tuesday","Wednesday","Thursday","Friday")
        val time = listOf<String>("07:00","09:00","11:00","13:00","14:00","16:00","18:00")

        val dayAdapter = ArrayAdapter(holder.binding.root.context, android.R.layout.simple_dropdown_item_1line, days)
        val timeAdapter = ArrayAdapter(holder.binding.root.context, android.R.layout.simple_dropdown_item_1line, time)

        holder.binding.etDay.adapter = dayAdapter
        holder.binding.etStartTime.adapter = timeAdapter
        holder.binding.etEndTime.adapter = timeAdapter



        holder.binding.etDay.setOnItemClickListener { _, _, pos, _ ->
            scheduleSlot.day = days[pos]
        }

        holder.binding.etStartTime.setOnItemClickListener { _, _, pos, _ ->
            scheduleSlot.startTime = time[pos]
        }

        holder.binding.etEndTime.setOnItemClickListener { _, _, pos, _ ->
            scheduleSlot.endTime = time[pos]
        }


    }

    override fun getItemCount(): Int = itemSchedule.size



    fun addTimeSlot() {
        itemSchedule.add(TimeSchedule())
        notifyItemInserted(itemSchedule.lastIndex)
    }
}