package com.project.tukcompass.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.project.tukcompass.adapters.MessageAdapter
import com.project.tukcompass.databinding.FragmentMessageBinding
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.models.EventModel

import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ChatsViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private val viewModel: ChatsViewModel by viewModels()
    private lateinit var adapter: MessageAdapter
    private lateinit var chatInfo: ChatModel
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatInfo = arguments?.getParcelable("chat") ?: ChatModel()

        binding.nameTxt.text = chatInfo.receiverName

        Glide.with(requireContext())
            .load(chatInfo.profileUrl)
            .into(binding.profilePic)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        sharedPrefManager = EncryptedSharedPrefManager(requireContext())

        val user = sharedPrefManager.getUser()!!

        adapter = MessageAdapter(currentUserId = user.userID)
        binding.chatMessagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatMessagesRecyclerView.adapter = adapter

        viewModel.loadMessages(chatInfo.chatID)

        lifecycleScope.launchWhenStarted {
            viewModel.messages.collect { resource ->
                when (resource) {

                    is Resource.Success -> {
                        val results = resource?.data ?: emptyList()
                        adapter.submitList(results)
                        if (results.isNotEmpty()) {
                            binding.chatMessagesRecyclerView.scrollToPosition(results.size - 1)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
        binding.addBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.sendBtn.setOnClickListener {
            val text = binding.messageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(
                    receiverID = chatInfo.receiverID,
                    type = "private",
                    message = text,
                    chatName = "",
                    chatAvatar = "",
                    imageUri = imageUri,
                    context = requireContext()
                )
                binding.messageInput.text?.clear()
                binding.imagePreview.visibility = View.GONE
            }
        }
        lifecycleScope.launch {
            viewModel.connected.filter { it }.first()
            viewModel.joinChat(chatInfo.chatID)
        }
    }
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.imagePreview.setImageURI(it)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }
    companion object {
        fun newInstance(chat: ChatModel) = MessageFragment().apply{
            arguments = bundleOf("chat" to chat)
        }
    }


}
