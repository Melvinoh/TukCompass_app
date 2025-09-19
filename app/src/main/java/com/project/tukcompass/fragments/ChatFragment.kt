package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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

    private var actionMode: ActionMode? = null
    private lateinit var chatAdapter: ChatAdapter


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

    private fun observeUserChats() {
        viewModel.chats.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val chats = response.data?.chats ?: emptyList()
                    if (!::chatAdapter.isInitialized) {
                        chatAdapter = ChatAdapter(chats) { chat ->
                            if (actionMode == null) {
                                val bundle = bundleOf("chat" to chat)
                                findNavController().navigate(R.id.messageFragment, bundle)
                            }
                        }.apply {
                            onItemLongClick = { pos -> enableChatActionMode(pos) }
                            onItemClickWrapper = { pos ->
                                if (actionMode != null) toggleChatSelection(pos)
                                else onItemClick(chats[pos])
                            }
                        }
                        binding.chatRecyclerView.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.chatRecyclerView.adapter = chatAdapter
                    } else {
                        chatAdapter.updateAdapter(chats)
                    }
                }

                else -> {}
            }
        }
    }
    private fun enableChatActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = requireActivity().startActionMode(chatActionModeCallback)
        }
        toggleChatSelection(position)
    }

    private fun toggleChatSelection(position: Int) {
        chatAdapter.toggleSelection(position)
        val count = chatAdapter.getSelectedChats().size
        if (count == 0) actionMode?.finish()
        else actionMode?.title = "$count selected"
    }
    private val chatActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

            binding.chatMessages.visibility = View.GONE
            mode?.menuInflater?.inflate(R.menu.contextual_menu, menu)

            return true
        }
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.action_delete -> {
                    val selected = chatAdapter.getSelectedChats()
                    Toast.makeText(requireContext(), "Delete ${selected.size} chats", Toast.LENGTH_SHORT).show()
                    selected.forEach { chat ->
                        viewModel.deleteChat(chat.chatID)
                    }

                    // Observe result
                    viewModel.deleteChatResult.observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), "Chat deleted", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {
                                // optional: show progress
                            }
                        }
                    }
                    mode?.finish()
                    true
                }
                else -> false
            }
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            chatAdapter.clearSelection()
            actionMode = null
            binding.chatMessages.visibility = View.VISIBLE
        }
    }


}
