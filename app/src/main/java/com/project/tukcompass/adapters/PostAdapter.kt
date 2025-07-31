package com.project.tukcompass.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.ViewholderPostBinding
import com.project.tukcompass.fragments.EventDetailsFragment
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.PostModel
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ClubViewModel
import com.project.tukcompass.viewModels.HomeViewModel
import kotlin.getValue

class PostAdapter(
    private var posts: List<PostModel>,
    private val onCommentClick: (PostModel) -> Unit

    ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ViewholderPostBinding ) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
        ): PostAdapter.ViewHolder {
            val binding = ViewholderPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)

        }

        override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
            val posts = posts[position]
            holder.binding.message.text = posts.posts_tb.description
            holder.binding.nameTxt.text = posts.posts_tb.user_tb.fname
            holder.binding.timeTxt.text = posts.posts_tb.date

            Glide.with(holder.itemView.context)
                .load(posts.posts_tb.imageURL)
                .into(holder.binding.postImage)

            Glide.with(holder.itemView.context)
                .load(posts.posts_tb.user_tb.profileUrl)
                .into(holder.binding.profilePic)

            holder.binding.comments.setOnClickListener {
                onCommentClick(posts)
            }
        }

        override fun getItemCount(): Int = posts.size

        fun updatePosts(newPost: List<PostModel>) {
            posts = newPost
            notifyDataSetChanged()
        }

    }



