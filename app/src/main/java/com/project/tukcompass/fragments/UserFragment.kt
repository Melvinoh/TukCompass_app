package com.project.tukcompass.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.ContactsAdapter
import com.project.tukcompass.databinding.FragmentUserBinding
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager
    private val viewModel: AuthViewModel by viewModels()

    private var socketConnectedInitiated = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserBinding.inflate(
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

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getUserContacts()
        observeUserContacts()

    }

    private fun observeUserContacts(){
        viewModel.contacts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val classContacts = response.data?.classmates ?: emptyList()
                    Log.d("chatLog", "${classContacts}")

                    binding.userViewholder.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    val adapter = binding.userViewholder.adapter as? ContactsAdapter

                    if (adapter == null) {
                        binding.userViewholder.adapter = ContactsAdapter(classContacts) { contact ->
                            val bundle = bundleOf("contacts" to contact )
                            findNavController().navigate(R.id.messageFragment, bundle)
                        }
                    } else {
                        adapter.updateAdapter(classContacts)
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