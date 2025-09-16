package com.project.tukcompass.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.tukcompass.fragments.CourseUnitsViewFragment
import com.project.tukcompass.models.YearTab

class CourseUnitViewPager(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var years: List<YearTab> = emptyList()

    override fun getItemCount(): Int = years.size

    override fun createFragment(position: Int): Fragment {
        val yearTab = years[position]
        return CourseUnitsViewFragment.newInstance(yearTab)
    }

    fun setData(newYears: List<YearTab>) {
        years = newYears
        notifyDataSetChanged()
    }

    fun getYearTab(position: Int): YearTab = years[position]
}
