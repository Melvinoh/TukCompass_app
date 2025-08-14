package com.project.tukcompass.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope


import com.project.tukcompass.databinding.FragmentEditProfileBinding
import com.project.tukcompass.models.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding


    private var currentUserId = 1 // Example: Logged-in user ID


    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val imageUrl = it.toString() // local URI string

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(layoutInflater,container,false)

        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        binding.backBtn.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.cameraEt.setOnClickListener{
            imagePicker.launch("image/*")
        }

        binding.update.setOnClickListener{
            formSubmit()
        }
    }



    private fun formSubmit() {

        val fname = binding.fName.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val mobile = binding.mobileEt.text.toString().trim()
        val country = binding.countryEt.text.toString().trim()
        val location = binding.locationEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.error ="invalid email format"
            return
        }else if(TextUtils.isEmpty(email)){
            binding.fName.error = "please enter email"
            return
        }else if(TextUtils.isEmpty(fname)){
            binding.fName.error = "please enter name"
            return
        }


        lifecycleScope.launch {

        }
    }

}


