package com.project.tukcompass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentMessageBinding
import com.project.tukcompass.models.ChatModel
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.models.MessageModel
import com.project.tukcompass.viewModels.ChatsViewModel
import com.project.tukcompass.viewModels.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private val viewModel: ChatsViewModel by viewModels()
    private lateinit var chatInfo: ChatModel



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

        chatInfo = arguments?.getParcelable("chat")!!

        binding.nameTxt.text = chatInfo.receiverName

        Glide.with(requireContext())
            .load(chatInfo.profileUrl)
            .into(binding.profilePic)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }




    }


}