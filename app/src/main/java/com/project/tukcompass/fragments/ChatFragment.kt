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
    private val viewModel: ChatsViewModel by viewModels()

    val chatList = listOf(

        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","Muturi melvin", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754753739/chat_media/3dlog.jpg","hey there am using whatsapp","3:40pm"),
        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","Boyani Beverly", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754410594/TukCompass/download_1.jpg  ","have you finished your project?","3:40pm"),
        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","omwami joshua", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754411336/TukCompass/download_2.png","zz cdtahi nitakuwa available","3:40pm"),

        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","Lucy Abwodtha", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754410961/TukCompass/download_4.jpg","kindly submmit your assignment on time","3:40pm"),

        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","milly Kagweru", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754410148/TukCompass/dance_baner","is the trip to mombasa confirmed ","3:40pm"),

        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","Eunice Njeri", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754410961/TukCompass/download_4.jpg","i feel like crying","3:40pm"),
        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","Dr Edwin", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754410414/TukCompass/3D-4.jpg ","Kindly present your work by tommorow","3:40pm"),
        ChatModel("78f28743-918c-43c9-832a-4a60451521fb","Mr Luke", "\"https://res.cloudinary.com/dkc2oujm6/image/upload/v1754753739/chat_media/3dlog.jpg","They will be a make up cat","3:40pm"),
        )


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

        viewModel.getUserchats()
        observeUserChats()

    }

    private fun observeChats() {

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.chatRecyclerView.adapter = ChatAdapter(chatList) { chat ->
            findNavController().navigate(R.id.messageFragment)

        }


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