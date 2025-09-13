package com.project.tukcompass.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.tukcompass.R
import com.project.tukcompass.adapters.EventsAdapter
import com.project.tukcompass.adapters.MediaAdapter
import com.project.tukcompass.adapters.MyGroupAdapter
import com.project.tukcompass.adapters.UnitContentAdapter
import com.project.tukcompass.databinding.FragmentUnitContentViewBinding
import com.project.tukcompass.models.ContentItem
import com.project.tukcompass.models.SessionDisplayItem
import com.project.tukcompass.utills.Resource
import com.project.tukcompass.viewModels.AcademicsViewHolder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.getValue
@AndroidEntryPoint
class UnitContentViewFragment : Fragment() {

    private lateinit var  binding: FragmentUnitContentViewBinding
    private lateinit var type: String
    private lateinit var unitDetails : SessionDisplayItem
    private val viewModel: AcademicsViewHolder by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString("type") ?: ""
            unitDetails = it.getParcelable("unitDetails") ?: SessionDisplayItem()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnitContentViewBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContent(unitDetails.unitOfferingID)
        observeContent()
    }

  companion object {
        fun newInstance(type: String, unitDetails: SessionDisplayItem): UnitContentViewFragment {
            return UnitContentViewFragment().apply {
                arguments = Bundle().apply {
                    putString("type", type)
                    putParcelable("unitDetails", unitDetails)
                }
            }
        }
    }

    private fun observeContent(){
        viewModel.content.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    val allContent = response.data.content
                    val content = when (type){
                        "pdf" -> allContent.pdf
                        "assignment" -> allContent.assignment
                        "video" -> allContent.video
                        "link" -> allContent.link
                        else -> emptyList()
                    }
                    if(type === "pdf" || type === "link"){
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        val adapter = binding.recyclerView.adapter as? UnitContentAdapter
                        if (adapter == null) {
                            binding.recyclerView.adapter = UnitContentAdapter(content, viewLifecycleOwner.lifecycleScope){ file ->
                                openPdfExternally(file)

                            }
                        } else {
                            adapter.updateAdapter(content)
                        }
                    }
                    if (type === "assignment" || type === "video"){
                        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                        val adapter = binding.recyclerView.adapter as? MediaAdapter
                        if (adapter == null) {
                            binding.recyclerView.adapter = MediaAdapter(content){

                            }
                        } else {
                            adapter.updateAdapter(content)
                        }
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
    private fun openPdfExternally(file: File) {
        val context = requireContext()
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(Intent.createChooser(intent, "Open PDF with..."))
        } catch (e: Exception) {
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }


}
