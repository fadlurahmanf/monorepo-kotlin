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
import com.fadlurahmanf.bebas_shared.extension.formatToEktpForm
import com.fadlurahmanf.bebas_ui.adapter.BebasPickerBottomsheetAdapter
import com.fadlurahmanf.bebas_ui.bottomsheet.BebasPickerBottomsheet
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetBebasPickerBinding
import com.fadlurahmanf.bebas_ui.dialog.DatePickerDialog
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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
        viewModel.selectedProvince.observe(this) {
            binding.ddProvince.setText(it?.label ?: "")
        }

        viewModel.selectedCity.observe(this) {
            binding.ddCity.setText(it?.label ?: "")
        }

        viewModel.ektpState.observe(this) {
            when (it) {
                is EktpFormState.FetchedProvinces -> {
                    dismissLoadingDialog()
                    showProvincesBottomsheet(it.provinces)
                }

                is EktpFormState.FetchedCities -> {
                    dismissLoadingDialog()
                    showCitiesBottomsheet(it.cities)
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
        binding.ddDatepicker.setOnClickListener {
            showDatePickerDialog()
        }


        binding.ddProvince.setOnClickListener {
            if (viewModel.provinces != null) {
                showProvincesBottomsheet(viewModel.provinces!!)
            } else {
                viewModel.fetchProvinces()
            }
        }

        binding.ddCity.setOnClickListener {
            if (viewModel.cities != null) {
                showCitiesBottomsheet(viewModel.cities!!)
            } else if (viewModel.selectedProvince.value != null) {
                viewModel.fetchCities(viewModel.selectedProvince.value?.id ?: "")
            }
        }
    }

    private var datePickerDialog: DatePickerDialog? = null

    private fun dismissDatePickerDialog() {
        datePickerDialog?.dismiss()
        datePickerDialog = null
    }

    private fun showDatePickerDialog() {
        dismissDatePickerDialog()

        datePickerDialog = DatePickerDialog()
        datePickerDialog?.setCallback(object : DatePickerDialog.Callback {
            override fun onClicked(year: Int, month: Int, dayOfMonth: Int, date: Date) {
                datePickerDialog?.dismiss()
                binding.ddDatepicker.setText(date.formatToEktpForm())
            }
        })
        datePickerDialog?.show(supportFragmentManager, DatePickerDialog::class.java.simpleName)
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

        pickerBottomsheet =
            BebasPickerBottomsheet(provinces, object : BebasPickerBottomsheetAdapter.Callback {
                override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                    dismissPickerBottomsheet()
                    viewModel.selectProvince(model)
                    binding.ddProvince.setText(model.label)
                }
            })
        showBottomsheet()
    }

    private fun showCitiesBottomsheet(cities: List<BebasItemPickerBottomsheetModel>) {
        dismissPickerBottomsheet()

        pickerBottomsheet =
            BebasPickerBottomsheet(cities, object : BebasPickerBottomsheetAdapter.Callback {
                override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                    dismissPickerBottomsheet()
                    viewModel.selectCity(model)
                    binding.ddCity.setText(model.label)
                }
            })
        showBottomsheet()
    }

    private fun showBottomsheet() {
        pickerBottomsheet?.show(
            supportFragmentManager,
            BottomsheetBebasPickerBinding::class.java.simpleName
        )
    }
}