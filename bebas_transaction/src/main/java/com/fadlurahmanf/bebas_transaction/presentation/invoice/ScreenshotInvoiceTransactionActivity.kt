package com.fadlurahmanf.bebas_transaction.presentation.invoice

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.fadlurahmanf.bebas_shared.data.argument.transaction.InvoiceTransactionArgumentConstant
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.transaction.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.databinding.ActivityScreenshotInvoiceTransactionBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import java.io.File
import java.io.FileOutputStream


class ScreenshotInvoiceTransactionActivity :
    BaseTransactionActivity<ActivityScreenshotInvoiceTransactionBinding>(
        ActivityScreenshotInvoiceTransactionBinding::inflate
    ) {

    private lateinit var argument: InvoiceTransactionArgument
    private lateinit var flow: InvoiceTransactionFlow

    private lateinit var directoryDownloaded: File
    private lateinit var directoryDownloadedPath: String

    private val handler = Handler(Looper.getMainLooper())

    override fun injectActivity() {

    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Flow = intent.getStringExtra(InvoiceTransactionArgumentConstant.FLOW)

        if (p0Flow == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(p0Flow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                InvoiceTransactionArgumentConstant.ARGUMENT,
                InvoiceTransactionArgument::class.java
            )
        } else {
            intent.getParcelableExtra(InvoiceTransactionArgumentConstant.ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_ARG"))
            return
        }

        argument = p0Arg

        when (VERSION.SDK_INT) {
            in 21..28 -> {
                directoryDownloaded =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                directoryDownloadedPath = directoryDownloaded.absolutePath
            }

            else -> {
                directoryDownloaded =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                directoryDownloadedPath = directoryDownloaded.absolutePath
            }
        }

        handler.postDelayed({
                                takeScreenshot()
                            }, 1000)
    }

    private fun takeScreenshot() {
        try {
            val v1 = window.decorView.rootView
            v1.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(v1.drawingCache)
            v1.isDrawingCacheEnabled = false

            val downloadedFile =
                File(directoryDownloadedPath + "/" + "BEBAS_INVOICE_${System.currentTimeMillis()}.png")

            val outputStream: FileOutputStream = FileOutputStream(downloadedFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            openScreenshot(downloadedFile)
        } catch (e: Throwable) {
            showForcedBackBottomsheet(BebasException.generalRC("TS_00"))
        }
    }

    private fun openScreenshot(imageFile: File) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
        }
        val uri = FileProvider.getUriForFile(applicationContext, "$packageName.provider", imageFile)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
        Log.d("BebasLogger", "MASUK_8")
    }
}