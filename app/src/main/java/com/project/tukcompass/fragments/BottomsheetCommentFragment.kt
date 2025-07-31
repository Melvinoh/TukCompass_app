package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.tukcompass.adapters.CommentsAdapter
import com.project.tukcompass.databinding.BottomsheetCommentsBinding
import com.project.tukcompass.models.CommentReqData
import com.project.tukcompass.models.CommentRequest
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class BottomsheetCommentFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetCommentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClubViewModel by viewModels()
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetCommentsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPrefManager = EncryptedSharedPrefManager(requireContext())

        postId = requireArguments().getString("postID")!!

        val reqBody: CommentRequest = CommentRequest(postId)

        val user = sharedPrefManager.getUser()

        Glide.with(this)
            .load(user?.profileUrl)
            .into(binding.avatarImage)


        binding.sendBtn.setOnClickListener {

            val comment = binding.editTxt.text.toString().trim()
            if (comment.isNotEmpty()) {
                val commentData = CommentReqData(postId, comment)
                viewModel.addComment(commentData)
                binding.editTxt.setText("")
            }


        }

        viewModel.getComment(reqBody)
        observeComments()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeComments() {

        viewModel.comment.observe(viewLifecycleOwner) { response ->

            Log.d("CommentResponseLog", "$response")
            when (response) {
                is Resource.Success -> {
                    val comment = response.data?.comments ?: emptyList()
                    Log.d("postLog", "${comment}")

                    binding.commentsRecyclerView.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    val adapter = binding.commentsRecyclerView.adapter as? CommentsAdapter
                    if (adapter == null) {
                        binding.commentsRecyclerView.adapter = CommentsAdapter(comment)
                    } else {
                        adapter.updateComments(comment)
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