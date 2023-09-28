package com.fadlurahmanf.mapp_example.presentation.mlkit

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import com.fadlurahmanf.mapp_example.databinding.ActivityFaceDetectorBinding
import com.fadlurahmanf.mapp_mlkit.domain.analyzer.FaceDetectorAnalyzer
import com.google.mlkit.vision.face.Face
import java.util.concurrent.Executors

class FaceDetectorActivity :
    BaseCameraActivity<ActivityFaceDetectorBinding>(ActivityFaceDetectorBinding::inflate) {
    override fun initCameraListener() {
        cameraProviderFuture().addListener({
            analyze()
        }, ContextCompat.getMainExecutor(this))
    }

    private lateinit var analyzer: ImageAnalysis

    private lateinit var image: ImageProxy
    private lateinit var faces: List<Face>
    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 3000)
            image.close()
        }

    }

    private val listener = object : FaceDetectorAnalyzer.Listener {

        override fun onSuccessGetFaces(faces: List<Face>, image: ImageProxy) {
            Log.d("MappActivity", "MASUK ON SUCCESS GET FACES ${faces.size}")
            faces.forEach {
                Log.d("MappActivity", "RIGHT EYE OPEN: ${it.rightEyeOpenProbability}")
                Log.d("MappActivity", "LEFT EYE OPEN: ${it.leftEyeOpenProbability}")
                Log.d("MappActivity", "SMILING: ${it.smilingProbability}")
                binding.tvResult.text =
                    "RIGHT EYE OPEN PROBABILITY: ${it.rightEyeOpenProbability}\nLEFT EYE OPEN PROBABILITY: ${it.leftEyeOpenProbability}\nSMILING PROBABILITY: ${it.smilingProbability}"
            }
            this@FaceDetectorActivity.image = image
            this@FaceDetectorActivity.faces = faces
            handler.removeCallbacks(onSuccessRunnable)
            handler.postDelayed(onSuccessRunnable, 3000)
        }

        override fun onFailedGetFaces(e: java.lang.Exception) {
            Log.d("MappActivity", "MASUK ON FAILED GET FACES: ${e.message}")
        }

    }

    override fun analyze() {
        Log.d("MappActivity", "MASUK ANALYZE")
        val cameraProvider = cameraProviderFuture().get()
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(binding.cameraView.surfaceProvider)
        }


        analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(cameraExecutor, FaceDetectorAnalyzer(listener))
            }
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        } catch (e: Throwable) {
            Log.d("MappActivity", "MASUK ERROR ANALYZE: ${e.message}")
        }
    }

    override fun injectActivity() {

    }

    override fun setup() {
        Log.d("MappActivity", "MASUK SETUP")
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
    }

}