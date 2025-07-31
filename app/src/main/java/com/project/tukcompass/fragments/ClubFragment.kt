package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.project.tukcompass.R
import com.project.tukcompass.adapters.CommentsAdapter
import com.project.tukcompass.adapters.PostAdapter
import com.project.tukcompass.databinding.FragmentClubBinding
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.apply

@AndroidEntryPoint
class ClubFragment : Fragment() {
    private lateinit var  binding: FragmentClubBinding

    private val viewModel: ClubViewModel by viewModels()
    private lateinit var club: ClubSportModel
    private lateinit  var clubId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClubBinding.inflate(inflater, container, false)

        return binding.root
    }

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        club = requireArguments().getParcelable<ClubSportModel>("club")!!
        clubId = club.clubSportsID

        Log.d("postResponseLog", "$clubId")

        super.onViewCreated(view, savedInstanceState)

        val appBarLayout = binding.appBarLayout
        val headerImage = binding.headerImage
        val headerContainer = binding.headerContainer
        val tabContainer = binding.tabContainer

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
            val totalScroll = appBar.totalScrollRange.toFloat()
            val currentScroll = -verticalOffset.toFloat()
            val collapseRatio = currentScroll / totalScroll

            headerImage.alpha = 1f - collapseRatio
            tabContainer.alpha = collapseRatio

            val scale = 1f - (collapseRatio * 0.2f) // scale from 1 to 0.7
            headerContainer.scaleX = scale
            headerContainer.scaleY = scale

            val translationY = -collapseRatio * 300 // adjust value to fit toolbar height
            headerContainer.translationY = translationY
        })

        viewModel.getPosts(clubId)
        observePosts()

        binding.fabAddPost.setOnClickListener {
            val bottomSheet = BottomSheetComposeFragment.newInstance(clubId)
            bottomSheet.show(parentFragmentManager, "ComposePostBottomSheet")
        }

    }
    private fun observePosts() {

        viewModel.posts.observe(viewLifecycleOwner) { response ->

            Log.d("postResponseLog", "$response")

            when (response) {

                is Resource.Success -> {
                    val post = response.data.posts
                    Log.d("postLog", "$post")

                    binding.recyclerViewPosts.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    val adapter = binding.recyclerViewPosts.adapter as? PostAdapter
                    if (adapter == null) {
                        binding.recyclerViewPosts.adapter = PostAdapter(post){ post ->

                            val bottomSheet = BottomsheetCommentFragment().apply {
                                arguments = Bundle().apply {
                                    putString("postID", post.postID)
                                }
                                Log.d("postID", "${post.postID}")
                            }
                            bottomSheet.show(parentFragmentManager, "commentBottomSheet")
                        }
                    } else {
                        adapter.updatePosts(post)
                    }

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    Log.d("error", "${response.message}")


                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    Log.d("loading", "Loading")
                }
                else -> {}
            }
        }
    }
}

