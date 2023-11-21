package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityPrepareOnboardingBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.camera_verification.EktpVerificationActivity
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import javax.inject.Inject


class PrepareOnboardingActivity :
    BaseOnboardingActivity<ActivityPrepareOnboardingBinding>(ActivityPrepareOnboardingBinding::inflate) {

    @Inject
    lateinit var viewModel: PrepareOnboardingViewModel
    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.btnNext.setOnClickListener {
            viewModel.updateIsFinishedPreparedOnBoarding(true)
            checkCameraPermission()
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                goToEktpCameraVerification()
            }
        }

    fun checkCameraPermission() {
        when (ContextCompat.checkSelfPermission(
            this.applicationContext,
            Manifest.permission.CAMERA
        )) {
            PackageManager.PERMISSION_GRANTED -> {
                goToEktpCameraVerification()
            }

            PackageManager.PERMISSION_DENIED -> {
                showFailedBottomsheet(
                    exception = BebasException(
                        title = "Information",
                        message = "Permission Camera",
                        buttonText = "Open Settings"
                    ),
                    callback = object : FailedBottomsheet.Callback {
                        override fun onButtonClicked() {
                            dismissFailedBottomsheet()
                            goToCameraPermission()
                        }

                    }
                )
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun goToEktpCameraVerification() {
        val intent = Intent(this, EktpVerificationActivity::class.java)
        startActivity(intent)
    }

    private fun goToCameraPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}