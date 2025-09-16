package com.project.tukcompass.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.project.tukcompass.R

import com.project.tukcompass.adapters.MessageAdapter
import com.project.tukcompass.databinding.FragmentMessageBinding
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.models.ContactsModel
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
    private lateinit var contactInfo: ContactsModel
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

        val contactArg = arguments?.getParcelable<ContactsModel>("contacts")
        val chatArg = arguments?.getParcelable<ChatModel>("chat")


        Log.d("MessageFragment", "Bundle: $arguments")
        Log.d("MessageFragment", "contactArg: $contactArg")


        sharedPrefManager = EncryptedSharedPrefManager(requireContext())

        val user = sharedPrefManager.getUser()!!

        adapter = MessageAdapter(currentUserId = user.userID)
        binding.chatMessagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatMessagesRecyclerView.adapter = adapter

        binding.emptyState.visibility = View.GONE

        if(chatArg != null ) {
            chatInfo = chatArg

            lifecycleScope.launch {
                viewModel.connected.filter { it }.first()
                viewModel.joinChat(chatArg.chatID)
            }
            binding.nameTxt.text = chatArg.receiverName
            Glide.with(requireContext())
                .load(chatArg.profileUrl)
                .into(binding.profilePic)


            viewModel.loadMessages(chatArg.chatID)
        }
        if(contactArg != null){
            contactInfo = contactArg
            binding.nameTxt.text = "${contactArg.fname} ${contactArg.sname}"
            Glide.with(requireContext())
                .load(contactArg.profileUrl)
                .placeholder(R.drawable.user_outlined)
                .into(binding.profilePic)

        }

        viewModel.messages.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val results = resource.data ?: emptyList()
                    adapter.submitList(results)
                    if (results.isNotEmpty()) {
                        binding.chatMessagesRecyclerView.scrollToPosition(results.size - 1)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> { /* Handle loading or ignore */ }
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }

        binding.addBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        val receiverID = when {
            chatArg != null -> chatArg.receiverID
            contactArg != null -> contactArg.userID
            else -> null
        }
        binding.sendBtn.setOnClickListener {
            val text = binding.messageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(
                    receiverID = receiverID!!,
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
