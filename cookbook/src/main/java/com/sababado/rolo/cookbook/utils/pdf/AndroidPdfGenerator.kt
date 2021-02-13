package com.sababado.rolo.cookbook.utils.pdf

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.util.Log
import android.view.View
import java.io.OutputStream
import kotlin.math.abs

private const val LINE_PADDING = 10f
private const val EDGE_PADDING = 16f

class AndroidPdfGenerator(documentTitle: String?) : PdfGenerator(documentTitle) {
    private val pdfDocument = PdfDocument()
    private var activePage: PdfDocument.Page? = null

    private var lastX: Float = EDGE_PADDING
    private var lastY: Float = 2f

    private val textPaint: Paint = Paint()

    init {
        // todo create cover page
        textPaint.color = Color.BLACK
    }

    override fun openCoverPage(pageWidth: Int, pageHeight: Int) {
        val pageInfo: PageInfo =
            PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
        activePage = pdfDocument.startPage(pageInfo)

        resetDrawingCoordinates()
    }

    override fun openPage(pageTitle: String?, pageWidth: Int, pageHeight: Int) {
        val pageInfo: PageInfo =
            PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
        activePage = pdfDocument.startPage(pageInfo)

        resetDrawingCoordinates()

        documentTitle?.let { writeText(it) }
        pageTitle?.let { writeText(it) }
    }

    private fun resetDrawingCoordinates() {
        lastX = EDGE_PADDING
        lastY = 2f
    }

    override fun closeDocument(outputStream: OutputStream) {
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
    }

    override fun writeText(text: String) {
        activePage?.let {
            val textBoundingBox = Rect()
            textPaint.getTextBounds(text, 0, text.length - 1, textBoundingBox)
            val newY: Float =
                lastY + LINE_PADDING + abs(textBoundingBox.bottom) + abs(textBoundingBox.top)

            val canvas = it.canvas
            canvas.drawText(text, lastX, newY, textPaint)
            lastY = newY
        } ?: Log.w("AndroidPdfGenerator", "You can't write content without opening a page first.")
    }

    override fun writeContent(view: View) {
        activePage?.let { view.draw(it.canvas) }
            ?: Log.w("AndroidPdfGenerator", "You can't write content without opening a page first.")
    }

    override fun writeContent(bitmap: Bitmap) {
        activePage?.canvas?.drawBitmap(bitmap, 0f, 0f, null)
            ?: Log.w("AndroidPdfGenerator", "You can't write content without opening a page first.")
    }

    override fun closePage() {
        pdfDocument.finishPage(activePage)
        activePage = null
    }
}