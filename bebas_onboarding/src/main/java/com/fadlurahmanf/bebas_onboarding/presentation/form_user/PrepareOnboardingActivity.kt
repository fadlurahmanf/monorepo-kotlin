package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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

    override fun onBebasCreate(savedInstanceState: Bundle?) {
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
                    showFailedBebasBottomsheet(it.exception)
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
                showFailedBebasBottomsheet(
                    exception = BebasException(
                        title = "Information",
                        message = "Permission Camera",
                        buttonText = "Open Settings"
                    ),
                    callback = object : FailedBottomsheet.Callback {
                        override fun onButtonClicked() {
                            dismissFailedBottomsheet()
                            goToAppPermission()
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
}