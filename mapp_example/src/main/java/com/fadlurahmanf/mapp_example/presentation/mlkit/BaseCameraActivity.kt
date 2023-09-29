package com.fadlurahmanf.mapp_example.presentation.mlkit

import android.os.Handler
import android.os.Looper
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity
import java.util.concurrent.ExecutorService

abstract class BaseCameraActivity<VB : ViewBinding>(inflate: MappInflateActivity<VB>) :
    BaseExampleActivity<VB>(inflate) {
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