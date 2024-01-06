package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationCameraBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingCameraActivity
import com.fadlurahmanf.bebas_shared.BebasSharedFake
import com.fadlurahmanf.core_mlkit.domain.analyzer.ImageLabelerAnalyzer
import com.fadlurahmanf.core_mlkit.external.CoreMlkitUtility
import com.google.mlkit.vision.label.ImageLabel
import java.util.concurrent.Executors
import javax.inject.Inject


@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class EktpVerificationCameraActivity :
    BaseOnboardingCameraActivity<ActivityEktpVerificationCameraBinding>(
        ActivityEktpVerificationCameraBinding::inflate
    ) {
    private lateinit var analyzer: ImageAnalysis
    private lateinit var preview: Preview
    private var torchEnabled: Boolean = false
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var isStartAnalyzer = false

    private lateinit var currentImageProxy: ImageProxy
    private lateinit var currentLabels: List<ImageLabel>
    private lateinit var currentBitmap: Bitmap

    @Inject
    lateinit var viewModel: EktpVerificationCameraViewModel

    override fun initCameraListener() {
        cameraProviderFuture().addListener({
                                               analyze()
                                           }, ContextCompat.getMainExecutor(this))
    }

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

            var isIdCardFound = false
            currentLabels.forEach {
                if ((it.text == "Mobile phone" || it.text == "Hand") && it.confidence >= 0.5f) {
                    isIdCardFound = true
                }
            }

            handler.removeCallbacks(onSuccessRunnable)
            if (!isIdCardFound) {
                handler.postDelayed(onSuccessRunnable, 3000)
            }

            if (isIdCardFound) {
                getOcrV2()
            }

        }

        override fun onFailedGetLabels(e: Exception) {
            Log.d("BebasLogger", "onFailedGetLabels: ${e.message}")
        }
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
                    ImageLabelerAnalyzer(listener)
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
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        }
    }

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        viewModel.ocrState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    val intent = Intent(this, EktpVerificationCameraResultActivity::class.java)
                    startActivity(intent)
                }

                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBebasBottomsheet(it.exception)
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                else -> {}
            }
        }
    }

    private fun getOcrV2() {
        val stringBase64Image = CoreMlkitUtility.getBase64FromBitmap(currentBitmap)
//         TODO(TAUFIK): BERMASALAH BASE64 NYA
        viewModel.getOCRv2(BebasSharedFake.base64ImageKtpFake)
    }

    private fun switchCamera() {
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        cameraProvider.unbindAll()
        if (isStartAnalyzer) {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        } else {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview
            )
        }
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