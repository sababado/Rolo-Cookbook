package com.sababado.rolo.cookbook.utils.pdf

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import java.io.FileOutputStream
import java.io.OutputStream

abstract class PdfGenerator(val documentTitle: String?) {
    companion object {
        /**
         * Get a new instance of a PdfGenerator.
         */
        fun createGenerator(documentTitle: String?): PdfGenerator {
            return AndroidPdfGenerator(documentTitle)
        }

        /**
         * Get a new instance of a managed PdfGenerator. This will open and close the PDF for you.
         */
        fun createGeneratorFromUri(
            context: Context,
            uri: Uri,
            documentTitle: String?,
            writeToPdfBlock: (pdf: PdfGenerator) -> Unit
        ) {
            context.contentResolver.openFileDescriptor(uri, "w")?.use {
                // use{} lets the document provider know you're done by automatically closing the stream
                val fos = FileOutputStream(it.fileDescriptor)
                val pdf = AndroidPdfGenerator(documentTitle)
                writeToPdfBlock(pdf)
                pdf.closeDocument(fos)
            }
        }
    }

    /**
     * Start a pdf page to begin writing to it. This is different from [openPage] as this does not include the document title.
     * The cover page should look different than the other pages and a title should be formatted differently.
     * Close with [closePage] before moving to another page.
     */
    abstract fun openCoverPage(pageWidth: Int = 595, pageHeight: Int = 842)

    /**
     * Start a pdf page to begin writing to it. Close with #closePage() before moving to another page.
     * @param pageTitle Optional title.
     */
    abstract fun openPage(pageTitle: String?, pageWidth: Int = 595, pageHeight: Int = 842)


    abstract fun writeText(text: String)

    abstract fun writeContent(view: View)
    abstract fun writeContent(bitmap: Bitmap)

    abstract fun closePage()

    /**
     * Saves all pages as long as they have been closed.
     */
    abstract fun closeDocument(outputStream: OutputStream)
}