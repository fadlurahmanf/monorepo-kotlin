package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_onboarding.data.flow.EktpVerificationFormFlow
import com.fadlurahmanf.bebas_onboarding.data.state.InitPrepareOnboardingState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityPrepareOnboardingBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.camera_verification.EktpVerificationCameraActivity
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
            checkCameraPermissionAndGoToEktpCameraVerification()
        }

        viewModel.initState.observe(this) {
            when (it) {
                is InitPrepareOnboardingState.SuccessToEktpCamera -> {
                    checkCameraPermissionAndGoToEktpCameraVerification()
                }

                is InitPrepareOnboardingState.SuccessToEktpVerification -> {
                    val intent = Intent(this, EktpVerificationFormActivity::class.java)
                    intent.putExtra(
                        EktpVerificationFormActivity.FROM_FLOW_ARG,
                        EktpVerificationFormFlow.FROM_PREPARE_ONBOARDING.name
                    )
                    startActivity(intent)
                }

                is InitPrepareOnboardingState.FAILED -> {
                    showFailedBottomsheet(it.exception)
                }
            }
        }

        viewModel.initPrepareOnboarding()
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                goToEktpCameraVerification()
            }
        }

    private fun checkCameraPermissionAndGoToEktpCameraVerification() {
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
        val intent = Intent(this, EktpVerificationCameraActivity::class.java)
        startActivity(intent)
    }

    private fun goToCameraPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}