package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import android.R.attr.label
import android.R.attr.text
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationBinding
import com.fadlurahmanf.bebas_onboarding.domain.analyzer.ImageLabelerAnalyzer
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingCameraActivity
import com.google.mlkit.vision.label.ImageLabel
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors


@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class EktpVerificationActivity :
    BaseOnboardingCameraActivity<ActivityEktpVerificationBinding>(ActivityEktpVerificationBinding::inflate) {
    private lateinit var analyzer: ImageAnalysis

    override fun initCameraListener() {
        cameraProviderFuture().addListener({
                                               analyze()
                                           }, ContextCompat.getMainExecutor(this))
    }

    private lateinit var currentImageProxy: ImageProxy
    private lateinit var currentLabels: List<ImageLabel>
    private lateinit var currentBitmap: Bitmap
    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 3000)
            currentImageProxy.close()
        }

    }

    private val listener = object : ImageLabelerAnalyzer.Listener {
        override fun onSuccessGetLabels(
            labels: List<ImageLabel>,
            image: ImageProxy,
            bitmapImage: Bitmap
        ) {
            currentImageProxy = image
            currentLabels = labels
            currentBitmap = bitmapImage

            Log.d("BebasLogger", "OBJECTS SIZE: ${labels.size}")
            var isIdCardFound = false
            currentLabels.forEach {
                Log.d("BebasLogger", "LABELS SIZE INSIDE OBJECT: ${it.text} & ${it.confidence}")
                if (it.text == "Mobile phone" && it.confidence >= 0.5f) {
                    isIdCardFound = true
                }
            }

            handler.removeCallbacks(onSuccessRunnable)
            if (!isIdCardFound) {
                handler.postDelayed(onSuccessRunnable, 3000)
            }

            if (isIdCardFound) {
                goToSummaryEktpVerificationActivity()
            }

        }

        override fun onFailedGetLabels(e: Exception) {
            Log.d("BebasLogger", "Labeling: ${e.message}")
        }
    }

    override fun analyze() {
        val cameraProvider = cameraProviderFuture().get()
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(binding.cameraView.surfaceProvider)
        }

        analyzer = ImageAnalysis.Builder()
            .setTargetResolution(Size(144, 176))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(
                    cameraExecutor,
                    ImageLabelerAnalyzer(listener)
                )
            }
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        } catch (e: Throwable) {
            Log.e("BebasLogger", "ERROR ANALYZE: ${e.message}")
        }
    }

    override fun injectActivity() {

    }

    override fun setup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
    }

    private fun goToSummaryEktpVerificationActivity() {
        val byteArrayOutputStream = ByteArrayOutputStream()
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        setResult(Activity.RESULT_OK)
        finish()
    }

}