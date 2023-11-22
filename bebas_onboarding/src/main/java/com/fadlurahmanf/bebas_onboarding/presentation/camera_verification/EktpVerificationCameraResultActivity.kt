package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import android.graphics.BitmapFactory
import android.util.Base64
import com.fadlurahmanf.bebas_onboarding.data.state.InitEktpCameraResult
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationCameraResultBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import javax.inject.Inject

class EktpVerificationCameraResultActivity :
    BaseOnboardingActivity<ActivityEktpVerificationCameraResultBinding>(
        ActivityEktpVerificationCameraResultBinding::inflate
    ) {

    @Inject
    lateinit var viewModel: EktpVerificationCameraViewModel

    companion object {
        const val BASE64_IMAGE_ARG = "BASE64_IMAGE_ARG"
    }

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        viewModel.initState.observe(this) {
            when (it) {
                is InitEktpCameraResult.SuccessLoadData -> {
                    val base64Image = it.base64Image
                    val imageBytes = Base64.decode(base64Image!!, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.ivResult.setImageBitmap(image)
                }

                is InitEktpCameraResult.FAILED -> {
                    showFailedBottomsheet(it.exception)
                }
            }
        }

        viewModel.initResult()

//        val base64Image = intent.getStringExtra(BASE64_IMAGE_ARG)
//        if (base64Image != null) {
//            val imageBytes = Base64.decode(base64Image, 0)
//            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//            binding.ivResult.setImageBitmap(image)
//        }
    }

}