package com.project.tukcompass.fragments


import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.project.tukcompass.R
import com.project.tukcompass.databinding.FragmentAnnouncementDetailsBinding
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.utills.PdfDownload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

class AnnouncementDetailsFragment : DialogFragment() {

    private  var _binding: FragmentAnnouncementDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.dialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnnouncementDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val announcement = arguments?.getParcelable<AnnouncementModel>(
            "item"
        ) ?: return dismiss()

        binding.titleTxt.text = announcement.title
        binding.textView7.text = announcement.message
        binding.close.setOnClickListener {
            dismiss()
        }

        val isPdf = announcement.fileUrl.endsWith(".pdf")

        if (isPdf) {
            binding.imgView.isVisible = false
            binding.pdfView.isVisible = true

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val file: File = PdfDownload.downloadPdfFile(requireContext(), announcement.fileUrl)
                    withContext(Dispatchers.Main) {
                        binding.pdfView.fromFile(file)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .spacing(16)
                            .fitEachPage(true)
                            .load()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            binding.imgView.isVisible = true
            binding.pdfView.isVisible = false

            Glide.with(this)
                .load(announcement.fileUrl)
                .into(binding.imgView)
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.overlay_bg)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private suspend fun downloadPdfFile(pdfUrl: String): File {
        val url = URL(pdfUrl)
        val connection = url.openConnection()
        val input = connection.getInputStream()
        val file = File(requireContext().cacheDir, "temp.pdf")
        file.outputStream().use { output ->
            input.copyTo(output)
        }
        return file
    }
}