package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.fadlurahmanf.bebas_onboarding.data.flow.EktpVerificationFormFlow
import com.fadlurahmanf.bebas_onboarding.data.state.EktpFormState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationFormBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.bottomsheet.BebasPickerBottomsheet
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetBebasPickerBinding
import java.lang.Exception
import javax.inject.Inject

class EktpVerificationFormActivity :
    BaseOnboardingActivity<ActivityEktpVerificationFormBinding>(ActivityEktpVerificationFormBinding::inflate) {

    @Inject
    lateinit var viewModel: EktpVerificationFormViewModel

    private var flow: EktpVerificationFormFlow = EktpVerificationFormFlow.UNKNOWN

    companion object {
        const val FROM_FLOW_ARG = "FROM_FLOW_ARG"
    }

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        flow = getFromFlow()

        if (flow == EktpVerificationFormFlow.UNKNOWN) {
            showForcedBackBottomsheet(BebasException.generalRC("FLOW_UNKNOWN"))
            return
        }

        initObserver()
        initAction()
    }

    private fun initObserver() {
        viewModel.ektpState.observe(this) {
            when (it) {
                is EktpFormState.FetchedProvinces -> {
                    dismissLoadingDialog()
                    showProvincesBottomsheet(it.provinces)
                }

                is EktpFormState.LOADING -> {
                    showLoadingDialog()
                }

                is EktpFormState.FAILED -> {
                    dismissLoadingDialog()
                }
            }
        }
    }

    private fun initAction() {
        binding.ddGender.setOnClickListener {
            Log.d("BebasLogger", "TES DD GENDER PROVINCES")
            viewModel.fetchProvinces()
        }
    }

    private fun getFromFlow(): EktpVerificationFormFlow {
        return try {
            intent.getStringExtra(FROM_FLOW_ARG)?.let {
                enumValueOf<EktpVerificationFormFlow>(it)
            } ?: EktpVerificationFormFlow.UNKNOWN
        } catch (e: Exception) {
            EktpVerificationFormFlow.UNKNOWN
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (flow == EktpVerificationFormFlow.FROM_EKTP_CAMERA_RESULT) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private var pickerBottomsheet: BebasPickerBottomsheet? = null

    private fun dismissPickerBottomsheet() {
        pickerBottomsheet?.dismiss()
        pickerBottomsheet = null
    }

    private fun showProvincesBottomsheet(provinces: List<BebasItemPickerBottomsheetModel>) {
        dismissPickerBottomsheet()
        pickerBottomsheet = BebasPickerBottomsheet(provinces)
        showBottomsheet()
    }

    private fun showBottomsheet() {
        pickerBottomsheet?.show(
            supportFragmentManager,
            BottomsheetBebasPickerBinding::class.java.simpleName
        )
    }
}