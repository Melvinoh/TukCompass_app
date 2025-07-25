package com.project.tukcompass.utills

import android.content.Context
import java.io.File
import java.net.URL

object PdfDownload {
     fun downloadPdfFile(context: Context, pdfUrl: String): File {
        val url = URL(pdfUrl)
        val connection = url.openConnection()
        val input = connection.getInputStream()
        val file = File(context.cacheDir, "temp.pdf")
        file.outputStream().use { output ->
            input.copyTo(output)
        }
        return file
    }
}
