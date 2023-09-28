package com.fadlurahmanf.mapp_example.presentation.mlkit

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import com.fadlurahmanf.mapp_example.databinding.ActivityObjectLabelingBinding
import com.fadlurahmanf.mapp_mlkit.domain.analyzer.ImageLabelAnalyzer
import com.google.mlkit.vision.label.ImageLabel
import java.lang.Exception
import java.util.concurrent.Executors

class ObjectLabelingActivity :
    BaseCameraActivity<ActivityObjectLabelingBinding>(ActivityObjectLabelingBinding::inflate) {
    override fun initCameraListener() {
        cameraProviderFuture().addListener({
            Log.d("MappLogger", "MASUK INIT CAMERA LISTENER")
            analyze()
        }, ContextCompat.getMainExecutor(this))
    }

    private lateinit var image: ImageProxy
    private lateinit var labels: List<ImageLabel>
    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 3000)
            image.close()
        }

    }

    private val listener = object : ImageLabelAnalyzer.Listener {
        override fun onSuccessGetLabels(labels: List<ImageLabel>, image: ImageProxy) {
            var text: String? = null
            var confidence: Float? = null
            Log.d("MappLogger", "MASUK ON SUCCESS GET LABELS: ${labels.size}")
            labels.forEach {
                Log.d("MappLogger", "LABELS: ${it.text} & ${it.confidence}")
                if (confidence == null || it.confidence >= (confidence ?: 0f)) {
                    confidence = it.confidence
                    text = it.text
                }
            }
            if (text != null && confidence != null) {
                binding.tvResult.text = "LABEL: ${text} & CONFIDENCE: ${confidence ?: 0 * 100}%"
            }
            this@ObjectLabelingActivity.image = image
            this@ObjectLabelingActivity.labels = labels
            handler.removeCallbacks(onSuccessRunnable)
            handler.postDelayed(onSuccessRunnable, 3000)
        }

        override fun onFailedGetLabels(e: Exception) {
            Log.d("MappLogger", "MASUK ON FAILED GET LABELS: ${e.message}")
        }
    }

    private lateinit var analyzer: ImageAnalysis

    override fun analyze() {
        Log.d("MappLogger", "MASUK ANALYZE")
        val cameraProvider = cameraProviderFuture().get()
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(binding.cameraView.surfaceProvider)
        }

        analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(cameraExecutor,
                    ImageLabelAnalyzer(listener)
                )
            }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        } catch (e: Throwable) {
            Log.e("MappActivity", "MASUK ERROR ANALYZE: ${e.message}")
        }
    }

    override fun injectActivity() {
        Log.d("MappLogger", "MASUK INJECT")
    }

    override fun setup() {
        Log.d("MappLogger", "MASUK SETUP")
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
    }
}