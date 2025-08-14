package com.project.tukcompass.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.tukcompass.R
import com.project.tukcompass.activities.LoginActivity
import com.project.tukcompass.databinding.FragmentAccountBinding
import com.project.tukcompass.utills.EncryptedSharedPrefManager


class AccountFragment : Fragment() {

    private lateinit var binding : FragmentAccountBinding
    private lateinit var sharedPrefManager: EncryptedSharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPrefManager = EncryptedSharedPrefManager(requireContext())

        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )
        binding.logout.setOnClickListener { 

            sharedPrefManager.clear()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.account.setOnClickListener {

            findNavController().navigate(R.id.editProfileFragment)
        }

        return binding.root
    }
}


