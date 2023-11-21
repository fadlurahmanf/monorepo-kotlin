package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingCameraActivity

class EktpVerificationActivity :
    BaseOnboardingCameraActivity<ActivityEktpVerificationBinding>(ActivityEktpVerificationBinding::inflate) {
    override fun initCameraListener() {
        cameraProviderFuture().addListener({
                                               analyze()
                                           }, ContextCompat.getMainExecutor(this))
    }

    override fun analyze() {
//        val cameraProvider = cameraProviderFuture().get()
//        val preview = Preview.Builder().build().apply {
//            setSurfaceProvider(binding.cameraView.surfaceProvider)
//        }
//
//        analyzer = ImageAnalysis.Builder()
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build().apply {
//                setAnalyzer(cameraExecutor,
//                            ImageLabelAnalyzer(listener)
//                )
//            }
//        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//        try {
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                this, cameraSelector, preview, analyzer
//            )
//        } catch (e: Throwable) {
//            Log.e("MappActivity", "MASUK ERROR ANALYZE: ${e.message}")
//        }
    }

    override fun injectActivity() {

    }

    override fun setup() {

    }

}