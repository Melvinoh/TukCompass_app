package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.ViewholderChatsBinding
import com.project.tukcompass.databinding.ViewholderUserBinding
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.models.ContactsModel

class ContactsAdapter(
    private var contacts: List<ContactsModel>,
    private val onItemClick: (ContactsModel) -> Unit
): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ViewholderUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ViewholderUserBinding.inflate(
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
        val contact = contacts[position]
        holder.binding.name.text = contact.fname
        holder.binding.text.text = contact.email

        Glide.with(holder.itemView.context)
            .load(contact.profileUrl)
            .placeholder(R.drawable.ic_account)
            .into(holder.binding.avatarImage)

        holder.itemView.setOnClickListener {
            onItemClick(contact)
        }
    }

    override fun getItemCount() : Int = contacts.size

    fun updateAdapter(newContacts: List<ContactsModel>) {
        contacts = newContacts
        notifyDataSetChanged()
    }


}