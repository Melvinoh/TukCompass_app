package com.project.tukcompass.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.project.tukcompass.databinding.ViewholderPdfBinding
import com.project.tukcompass.models.ContentItem
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import androidx.core.graphics.createBitmap

class UnitContentAdapter(
    private var unitContent: List<ContentItem>
) : RecyclerView.Adapter<UnitContentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderPdfBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContentItem) {
            binding.fileName.text = item.title
            binding.fileTitle.text = item.title

            binding.downloadBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val file = downloadPdf(item.url, item.title)
                        val pageCount = getPdfPageCount(file)
                        binding.pageCount.text = "$pageCount pages"

                        withContext(Dispatchers.Main) {
                            val file = downloadPdf(item.url, item.title)
                            openPdfExternally(file)
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(binding.root.context, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

        private fun downloadPdf(url: String, title: String): File {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw Exception("Download failed")

            val file = File(
                binding.root.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "$title.pdf"
            )
            response.body?.byteStream()?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return file
        }

        private fun getPdfPageCount(file: File): Int {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            val count = renderer.pageCount
            renderer.close()
            pfd.close()
            return count
        }

        private fun renderFirstPage(file: File): Bitmap? {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            val page = renderer.openPage(0)

            val bitmap = createBitmap(page.width, page.height)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            page.close()
            renderer.close()
            pfd.close()

            return bitmap
        }

        private fun openPdfExternally(file: File) {
            val context = binding.root.context
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(intent, "Open PDF with..."))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderPdfBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(unitContent[position])
    }

    override fun getItemCount(): Int = unitContent.size

    fun updateAdapter(newContent: List<ContentItem>) {
        this.unitContent = newContent
        notifyDataSetChanged()
    }
}
