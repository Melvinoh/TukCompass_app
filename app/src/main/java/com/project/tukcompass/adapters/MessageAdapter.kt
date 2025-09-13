package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.databinding.ViewholderReceiverBinding
import com.project.tukcompass.databinding.ViewholderSenderBinding
import com.project.tukcompass.models.MessageModel

class MessageAdapter(
    private val currentUserId: String ?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<MessageModel>()

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    fun submitList(newMessages: List<MessageModel>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderID == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ViewholderSenderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SentMessageViewHolder(binding)
        } else {
            val binding = ViewholderReceiverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SentMessageViewHolder -> holder.bind(message)
            is ReceivedMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    // --- Sent ViewHolder
    class SentMessageViewHolder(private val binding: ViewholderSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageModel) {
            if (!message.mediaUrl.isNullOrEmpty()) {
                binding.imageMedia.visibility = View.VISIBLE
                binding.textMessage.visibility = View.VISIBLE
                binding.textMessage.text = message.messageContent

                Glide.with(binding.imageMedia.context)
                    .load(message.mediaUrl)
                    .into(binding.imageMedia)
            } else {
                binding.textMessage.visibility = View.VISIBLE
                binding.textMessage.text = message.messageContent
                binding.imageMedia.visibility = View.GONE
            }
        }
    }
    class ReceivedMessageViewHolder(private val binding: ViewholderReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageModel) {
            if (!message.mediaUrl.isNullOrEmpty()) {
                binding.imageMedia.visibility = View.VISIBLE
                binding.textMessage.visibility = View.VISIBLE
                binding.textMessage.text = message.messageContent

                Glide.with(binding.imageMedia.context)
                    .load(message.mediaUrl)
                    .into(binding.imageMedia)
            } else {
                binding.textMessage.visibility = View.VISIBLE
                binding.textMessage.text = message.messageContent
                binding.imageMedia.visibility = View.GONE
            }
        }
    }
}

