package com.project.tukcompass.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.tukcompass.databinding.BottomsheetComposeBinding
import com.project.tukcompass.viewModels.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetComposeFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetComposeBinding
    private val viewModel: ClubViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private lateinit var clubId: String


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){

        it?.let {
            it.let {
                selectedImageUri = it
                binding.previewImage.setImageURI(it)
                binding.previewImage.visibility = View.VISIBLE
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetComposeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        clubId = arguments?.getString("clubId")!!

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.submitPost.setOnClickListener {
            val description = binding.editTxt.text.toString()


            viewModel.createPost(description,clubId, selectedImageUri,requireContext())
        }
        viewModel.postCreated.observe(viewLifecycleOwner) { created ->
            if (created) {
                dismiss()
            }
        }


    }
    companion object {
        fun newInstance(clubId: String): BottomSheetComposeFragment {
            val fragment = BottomSheetComposeFragment()
            val args = Bundle()
            args.putString("clubId", clubId)
            fragment.arguments = args
            return fragment
        }
    }
}