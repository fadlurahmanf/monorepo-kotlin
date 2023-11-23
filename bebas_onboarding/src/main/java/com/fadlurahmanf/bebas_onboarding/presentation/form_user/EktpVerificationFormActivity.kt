package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.flow.EktpVerificationFormFlow
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationFormBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import java.lang.Exception

class EktpVerificationFormActivity :
    BaseOnboardingActivity<ActivityEktpVerificationFormBinding>(ActivityEktpVerificationFormBinding::inflate) {

    private var flow: EktpVerificationFormFlow = EktpVerificationFormFlow.UNKNOWN

    companion object {
        const val FROM_FLOW_ARG = "FROM_FLOW_ARG"
    }

    override fun injectActivity() {

    }

    override fun setup() {
        flow = getFromFlow()

        Log.d("BebasLogger", "fromFlow: $flow")
        if (flow == EktpVerificationFormFlow.UNKNOWN) {
            showForcedBackBottomsheet(BebasException.generalRC("FLOW_UNKNOWN"))
            return
        }
    }

    private fun getFromFlow(): EktpVerificationFormFlow {
        return try {
            intent.getStringExtra(FROM_FLOW_ARG)?.let {
                enumValueOf<EktpVerificationFormFlow>(it)
            } ?: EktpVerificationFormFlow.UNKNOWN
        } catch (e: Exception) {
            Log.e("BebasLogger", "getFromFlow: $e")
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

}