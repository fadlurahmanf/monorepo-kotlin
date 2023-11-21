package com.fadlurahmanf.bebas_onboarding.presentation

import android.os.Handler
import android.os.Looper
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_ui.activity.BebasInflateActivity
import java.util.concurrent.ExecutorService

abstract class BaseOnboardingCameraActivity<VB : ViewBinding>(inflate: BebasInflateActivity<VB>) :
    BaseOnboardingActivity<VB>(inflate) {
    lateinit var cameraExecutor: ExecutorService

    val handler = Handler(Looper.getMainLooper())

    abstract fun initCameraListener()

    abstract fun analyze()

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    open fun cameraProviderFuture() = ProcessCameraProvider.getInstance(this)
}