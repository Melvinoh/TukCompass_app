package com.project.tukcompass.adapters


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.project.tukcompass.databinding.VieholderScheduleBinding
import com.project.tukcompass.models.GroupedSchedulePayload
import com.project.tukcompass.models.ScheduleBlock



class ScheduleAdapter(
    private val schedules: MutableList<ScheduleBlock>,
    private val fetchCourses: (String, Int) -> Unit, // (typed text, position)
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(val binding: VieholderScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var scheduleSlotAdapter: ScheduleSlotAdapter

        fun bind(schedule: ScheduleBlock, position: Int) {
            val courseAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
            binding.courseMultiSelect.setAdapter(courseAdapter)
            binding.courseMultiSelect.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())


            binding.ChipGroup.removeAllViews()


            binding.courseMultiSelect.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val query = s?.split(",")?.last()?.trim()
                    if (!query.isNullOrBlank() && query.length >= 2) {
                        fetchCourses(query, adapterPosition)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })


            binding.courseMultiSelect.setOnItemClickListener { _, _, pos, _ ->
                val selectedCourse = courseAdapter.getItem(pos)
                if (!selectedCourse.isNullOrBlank() && !schedule.courseNames.contains(selectedCourse)) {
                    schedule.courseNames.add(selectedCourse)
                    addCourseChip(selectedCourse, binding, schedule)
                }
                binding.courseMultiSelect.text.clear()
            }

            scheduleSlotAdapter = ScheduleSlotAdapter(schedule.timeSlots)
            binding.recyclerTimeSlots.adapter = scheduleSlotAdapter

            binding.btnAddSlot.setOnClickListener {
                scheduleSlotAdapter.addTimeSlot()
            }

        }

        fun updateCourseSuggestions(suggestions: List<String>) {
            val adapter = ArrayAdapter(binding.root.context, android.R.layout.simple_dropdown_item_1line, suggestions)
            binding.courseMultiSelect.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }
    }

    private val viewHolders = mutableMapOf<Int, ScheduleViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = VieholderScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding).also {
            viewHolders[it.adapterPosition] = it
        }
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(schedules[position], position)
    }

    override fun getItemCount(): Int = schedules.size

    fun updateCourseSuggestions(position: Int, suggestions: List<String>) {
        viewHolders[position]?.updateCourseSuggestions(suggestions)
    }

    fun getSchedulePayload(): List<GroupedSchedulePayload> {
        return schedules.map {
            GroupedSchedulePayload(
                courseNames = it.courseNames,
                timeSlots = it.timeSlots
            )
        }
    }

    private fun addCourseChip(course: String, binding: VieholderScheduleBinding, schedule: ScheduleBlock) {
        val chip = Chip(binding.root.context).apply {
            text = course
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                schedule.courseNames.remove(course)
                binding.ChipGroup.removeView(this)
            }
        }
        binding.ChipGroup.addView(chip)
    }
}

