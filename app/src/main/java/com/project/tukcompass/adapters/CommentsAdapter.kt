package com.project.tukcompass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.project.tukcompass.databinding.ViewholderCommentsBinding
import com.project.tukcompass.models.CommentModel
import com.project.tukcompass.models.PostModel

class CommentsAdapter(
    private var comments: List<CommentModel>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderCommentsBinding ) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentsAdapter.ViewHolder {
        val binding = ViewholderCommentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {

        val comments = comments[position]
        holder.binding.nameTxt.text = comments.user_tb.fname

        holder.binding.commentsTxt.text = comments.description
        Glide.with(holder.itemView.context)
            .load(comments.user_tb.profileUrl)
            .into(holder.binding.profilePic)

    }

    override fun getItemCount(): Int = comments.size

    fun updateComments(newComment: List<CommentModel>) {
        comments = newComment
        notifyDataSetChanged()
    }
}
