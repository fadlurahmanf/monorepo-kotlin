package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import android.graphics.Bitmap
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityFaceVerificationBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingCameraActivity
import com.fadlurahmanf.core_mlkit.domain.analyzer.FaceDetectorAnalyzer
import com.fadlurahmanf.core_mlkit.external.CoreMlkitUtility
import com.google.mlkit.vision.face.Face
import java.util.concurrent.Executors

class FaceVerificationActivity :
    BaseOnboardingCameraActivity<ActivityFaceVerificationBinding>(ActivityFaceVerificationBinding::inflate) {
    private lateinit var analyzer: ImageAnalysis
    private lateinit var preview: Preview
    private var torchEnabled: Boolean = false
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var isStartAnalyzer = false

    private lateinit var currentImageProxy: ImageProxy
    private lateinit var currentFaces: List<Face>
    private lateinit var currentBitmap: Bitmap

    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 3000)
            currentImageProxy.close()
        }
    }

    private val listener = object : FaceDetectorAnalyzer.Listener {

        override fun onSuccessGetFaces(faces: List<Face>, image: ImageProxy, bitmapImage: Bitmap) {
            currentImageProxy = image
            currentFaces = faces
            currentBitmap = bitmapImage

            var isFaceFound = false
            currentFaces.forEach {
                val rightEyeOpenProbability = it.rightEyeOpenProbability ?: 0f
                val leftEyeOpenProbability = it.leftEyeOpenProbability ?: 0f
                val smilingProbability = it.smilingProbability ?: 0f
                Log.d("BebasLogger", "RIGHT: $rightEyeOpenProbability")
                Log.d("BebasLogger", "LEFT: $leftEyeOpenProbability")
                Log.d("BebasLogger", "SMILE: $smilingProbability")
                if (leftEyeOpenProbability > 0.5f && rightEyeOpenProbability > 0.5f && smilingProbability > 0.2f) {
                    isFaceFound = true
                }
            }

            handler.removeCallbacks(onSuccessRunnable)
            if (!isFaceFound) {
                handler.postDelayed(onSuccessRunnable, 3000)
            }

            if (isFaceFound) {
                processFace()
            }
        }

        override fun onFailedGetFaces(e: Exception) {
            Log.d("BebasLogger", "onFailedGetFaces: ${e.message}")
        }
    }

    private fun processFace() {
        val stringBase64Image = CoreMlkitUtility.getBase64FromBitmap(currentBitmap)
        // TODO: TAUFIK PROCESS HERE
    }

    override fun initCameraListener() {
        cameraProviderFuture().addListener({
                                               analyze()
                                           }, ContextCompat.getMainExecutor(this))
    }

    override fun analyze() {
        cameraProvider = cameraProviderFuture().get()
        preview = Preview.Builder().build().apply {
            setSurfaceProvider(binding.cameraView.surfaceProvider)
        }

        analyzer = ImageAnalysis.Builder()
            .setTargetResolution(Size(144 * 2, 176 * 2))

            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(
                    cameraExecutor,
                    FaceDetectorAnalyzer(listener)
                )
            }
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview
            )
            setupCameraInfoObserver()
        } catch (e: Throwable) {
            Log.e("BebasLogger", "ERROR ANALYZE: ${e.message}")
        }
    }

    private fun setupCameraInfoObserver() {
        binding.ivCamera.visibility = View.VISIBLE
        camera.cameraInfo.torchState.observe(this) {
            if (it == TorchState.ON) {
                binding.ivFlash.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.round_flash_on_24
                    )
                )
            } else {
                binding.ivFlash.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.round_flash_off_24
                    )
                )
            }
        }

        binding.ivFlash.setOnClickListener {
            if (!torchEnabled) {
                if (camera.cameraInfo.hasFlashUnit()) {
                    torchEnabled = true
                    camera.cameraControl.enableTorch(true)
                }
            } else {
                torchEnabled = false
                camera.cameraControl.enableTorch(false)
            }
        }

        binding.ivSwitchCamera.setOnClickListener {
            switchCamera()
        }

        binding.ivCamera.setOnClickListener {
            binding.ivCamera.visibility = View.INVISIBLE
            isStartAnalyzer = true
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        }
    }

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {}

    private fun switchCamera() {
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            this, cameraSelector, preview, if (isStartAnalyzer) analyzer else null
        )
    }

    override fun onStop() {
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        isStartAnalyzer = false
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
    }

}