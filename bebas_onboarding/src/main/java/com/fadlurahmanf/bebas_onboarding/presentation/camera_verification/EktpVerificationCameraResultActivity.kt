package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.bebas_onboarding.data.flow.EktpVerificationFormFlow
import com.fadlurahmanf.bebas_onboarding.data.state.InitEktpCameraResult
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationCameraResultBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.form_user.EktpVerificationFormActivity
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

    override fun onBebasCreate(savedInstanceState: Bundle?) {
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

        binding.btnNext.setOnClickListener {
            viewModel.updateIsFinishedEktpCameraVerification(true)
            val intent = Intent(this, EktpVerificationFormActivity::class.java)
            intent.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(
                    EktpVerificationFormActivity.FROM_FLOW_ARG,
                    EktpVerificationFormFlow.FROM_EKTP_CAMERA_RESULT.name
                )
            }
            ektpFormLauncher.launch(intent)
        }
    }

    private val ektpFormLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }

}