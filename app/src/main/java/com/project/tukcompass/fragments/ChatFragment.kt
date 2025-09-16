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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.ChatAdapter
import com.project.tukcompass.databinding.FragmentChatBinding
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.ChatsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager
    private val viewModel: ChatsViewModel by viewModels()
    private val serverUrl = "http://10.0.2.2:3000"
    private var socketConnectedInitiated = false


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

        val token = sharedPrefManager.getToken()!!
        binding.fabNewMessage.setOnClickListener {
            findNavController().navigate(R.id.UserFragment,)
        }

        viewModel.getUserchats()
        observeUserChats()
        viewModel.connectSocket(serverUrl, token)
        binding.emptyState.visibility = View.GONE

    }

    private fun observeUserChats(){
        viewModel.chats.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val chats = response.data?.chats ?: emptyList()
                    Log.d("chatLog", "${chats}")

                    binding.chatRecyclerView.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    binding.emptyState.visibility = View.GONE
                    val adapter = binding.chatRecyclerView.adapter as? ChatAdapter

                    if (adapter == null) {
                        binding.chatRecyclerView.adapter = ChatAdapter(chats) { chat ->

                            lifecycleScope.launch {
                                viewModel.connected.filter { it }.first()
                                viewModel.joinChat(chat.chatID)
                            }
                            val bundle = bundleOf("chat" to chat)
                            findNavController().navigate(R.id.messageFragment, bundle)
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
    private fun displayFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)//this container is in main activity
            .addToBackStack("null")
            .commit()
    }
}
