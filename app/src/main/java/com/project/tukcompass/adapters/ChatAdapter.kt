package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.VieholderCategoryBinding
import com.project.tukcompass.databinding.ViewholderChatsBinding
import com.project.tukcompass.models.CategoryModel
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.models.ContentItem

class ChatAdapter(
    private var chats: List<ChatModel>,
    val onItemClick: (ChatModel) -> Unit
): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()
    var onItemLongClick: ((Int) -> Unit)? = null
    var onItemClickWrapper: ((Int) -> Unit)? = null


    inner class ViewHolder(val binding: ViewholderChatsBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ViewholderChatsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.binding.name.text = chat.receiverName
        holder.binding.text.text = chat.message

        Glide.with(holder.itemView.context)
            .load(chat.profileUrl)
            .placeholder(R.drawable.ic_account)
            .into(holder.binding.avatarImage)

        holder.itemView.setOnClickListener { onItemClickWrapper?.invoke(position) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position)
            true
        }
    }

    override fun getItemCount() : Int = chats.size

    fun updateAdapter(newChats: List<ChatModel>) {
        chats = newChats
        notifyDataSetChanged()
    }
    fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) selectedItems.remove(position)
        else selectedItems.add(position)
        notifyItemChanged(position)
    }

    fun clearSelection() {
        val copy = selectedItems.toSet()
        selectedItems.clear()
        copy.forEach { notifyItemChanged(it) }
    }

    fun getSelectedChats(): List<ChatModel> {
        return selectedItems.map { chats[it] }
    }


}