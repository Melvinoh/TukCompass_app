package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.ChatAdapter
import com.project.tukcompass.databinding.FragmentChatBinding
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ChatsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager
    private val viewModel: ChatsViewModel by viewModels()

    private val serverUrl = "http://10.0.2.2:3000" 
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefManager = EncryptedSharedPrefManager(requireContext())

        val token = sharedPrefManager.getToken()

        if (token.isNotEmpty()) {
            viewModel.connectWithExistingToken(serverUrl, token)
        }

        viewModel.getUserchats()
        observeUserChats()

    }

   
    private fun observeUserChats(){
        viewModel.chats.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val chats = response.data?.chats?: emptyList()
                    Log.d("chatLog", "${chats}")

                    binding.chatRecyclerView.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    val adapter = binding.chatRecyclerView.adapter as? ChatAdapter

                    if (adapter == null) {
                        binding.chatRecyclerView.adapter = ChatAdapter(chats) { chat ->

                            lifecycleScope.launchWhenStarted {
                                viewModel.connected.collect { isConnected ->
                                    if (isConnected) viewModel.joinChat(chat.chatID)
                                }
                            }
                            val bundle = bundleOf("chat" to chat)
                            findNavController().navigate(R.id.messageFragment,bundle)
                        }
                    } else {
                        adapter.updateAdapter(chats)
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
