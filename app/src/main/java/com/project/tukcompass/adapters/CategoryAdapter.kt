package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.VieholderCategoryBinding
import com.project.tukcompass.databinding.ViewholderGroupsBinding
import com.project.tukcompass.models.CategoryModel

class CategoryAdapter(
    private val categories: List<CategoryModel>,
    private val onItemClick: (CategoryModel) -> Unit
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: VieholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = VieholderCategoryBinding.inflate(
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
        val category = categories[position]
        holder.binding.groupName.text = category.title
        holder.binding.groupIcon.setImageResource(category.iconResId)
        holder.itemView.setOnClickListener {
            onItemClick(category)
        }
    }

    override fun getItemCount() : Int = categories.size


}

