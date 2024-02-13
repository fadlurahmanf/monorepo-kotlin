package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.flow.EktpVerificationFormFlow
import com.fadlurahmanf.bebas_onboarding.data.state.EktpFormState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEktpVerificationFormBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.formatToEktpForm
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import com.fadlurahmanf.bebas_ui.adapter.BebasPickerBottomsheetAdapter
import com.fadlurahmanf.bebas_ui.bottomsheet.BebasPickerBottomsheet
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetBebasPickerBinding
import com.fadlurahmanf.bebas_ui.dialog.DatePickerDialog
import com.fadlurahmanf.bebas_ui.edittext.BebasEdittext
import com.fadlurahmanf.bebas_ui.edittext.BebasMaskingEdittext
import java.util.Date
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

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        flow = getFromFlow()

        if (flow == EktpVerificationFormFlow.UNKNOWN) {
            showForcedBackBottomsheet(BebasException.generalRC("FLOW_UNKNOWN"))
            return
        }

        initFieldListener()
        initObserver()
        initAction()

        viewModel.setNik("3511333333333333")
        binding.etNik.text = "3511333333333333"
        viewModel.setFullName("test bmas 1")
        binding.etFullname.text = "test bmas 1"
        viewModel.setBirthPlaceState("jakarta")
        binding.etBirthplace.text = "jakarta"
        viewModel.setBirthDateState(982667929983L)
        binding.ddBirthdate.setText("20-02-2001")
        viewModel.setGender(BebasItemPickerBottomsheetModel(id = "M", label = "LAKI-LAKI"))
        binding.ddGender.setText("LAKI-LAKI")
        viewModel.selectProvince(BebasItemPickerBottomsheetModel(id = "12", label = "JAWA TIMUR"))
        viewModel.selectCity(BebasItemPickerBottomsheetModel(id = "1213", label = "MALANG"))
        viewModel.selectSubDistrict(
            BebasItemPickerBottomsheetModel(
                id = "121303",
                label = "BANTUR"
            )
        )
        viewModel.selectWard(BebasItemPickerBottomsheetModel(id = "12130303", label = "BANTUR"))
        viewModel.setAddress("fake address bmas")
        binding.etAddress.text = "fake address bmas"
        viewModel.setRtRw("001/002")
        binding.etRtRw.text = "001/002"

//        viewModel.initData()
    }

    private fun initFieldListener() {
        binding.etNik.addTextChangedListener(object :
                                                 BebasMaskingEdittext.BebasMaskingEdittextTextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(
                s: Editable?,
                formattedText: String?,
                unformattedText: String?
            ) {
                viewModel.processFormThroughButton = true
                viewModel.setNik(unformattedText ?: "")
            }

        })

        binding.etFullname.addTextChangedListener(object :
                                                      BebasEdittext.BebasEdittextTextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.processFormThroughButton = true
                viewModel.setFullName(s?.toString() ?: "")
            }

        })

        binding.etBirthplace.addTextChangedListener(object :
                                                        BebasEdittext.BebasEdittextTextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.processFormThroughButton = true
                viewModel.setBirthPlaceState(s?.toString() ?: "")
            }

        })
    }

    private fun initObserver() {
        viewModel.nikState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etNik.setError(
                        getString(
                            it.idRawStringRes,
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.EMPTY -> {
                    binding.etNik.setError(
                        getString(
                            R.string.error_general_message_form_empty,
                            "NIK"
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etNik.removeError()
                }
            }
        }

        viewModel.fullNameState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etFullname.setError(
                        getString(
                            it.idRawStringRes,
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.EMPTY -> {
                    binding.etFullname.setError(
                        getString(
                            R.string.error_general_message_form_empty,
                            "Full Name"
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etFullname.removeError()
                }
            }
        }

        viewModel.birthPlaceState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etBirthplace.setError(
                        getString(
                            it.idRawStringRes,
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.EMPTY -> {
                    binding.etBirthplace.setError(
                        getString(
                            R.string.error_general_message_form_empty,
                            "Birth Place"
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etBirthplace.removeError()
                }
            }
        }


        viewModel.initState.observe(this) {
            when (it) {
                is EktpFormState.FetchedLocalData -> {
                    binding.etNik.text = it.nik ?: ""
                    binding.etFullname.text = it.fullName ?: ""
                    binding.etBirthplace.text = it.birthPlace ?: ""

                    if (it.birthDate != null) {
                        binding.ddBirthdate.setText(it.birthDate)
                    }

                    if (viewModel.initSelectedProvinceLabel != null) {
                        viewModel.fetchProvinces(selectedProvinceLabel = it.province)
                    }
                }

                else -> {}
            }
        }
        viewModel.selectedProvince.observe(this) {
            Log.d("BebasLogger", "PROVINCE: $it")
            binding.ddProvince.setText(it?.label ?: "")
        }

        viewModel.selectedCity.observe(this) {
            Log.d("BebasLogger", "CITY: $it")
            binding.ddCity.setText(it?.label ?: "")
        }

        viewModel.selectedSubDistrict.observe(this) {
            Log.d("BebasLogger", "SUBDISTRICT: $it")
            binding.ddSubdistrict.setText(it?.label ?: "")
        }

        viewModel.selectedWard.observe(this) {
            Log.d("BebasLogger", "WARD: $it")
            binding.ddWard.setText(it?.label ?: "")
        }

        viewModel.ektpState.observe(this) {
            when (it) {
                is EktpFormState.FetchedProvinces -> {
                    dismissLoadingDialog()
                    showProvincesBottomsheet(it.provinces)
                }

                is EktpFormState.FetchedProvincesAndSelect -> {
                    dismissLoadingDialog()

                    if (viewModel.selectedProvince.value?.id != null && viewModel.initSelectedCityLabel != null) {
                        viewModel.fetchCities(
                            provinceId = viewModel.selectedProvince.value?.id ?: "",
                            selectedCityLabel = viewModel.initSelectedCityLabel
                        )
                    }
                }

                is EktpFormState.FetchedCities -> {
                    dismissLoadingDialog()
                    showCitiesBottomsheet(it.cities)
                }

                is EktpFormState.FetchedCitiesAndSelect -> {
                    dismissLoadingDialog()

                    if (viewModel.selectedCity.value?.id != null && viewModel.initSelectedSubDistrictLabel != null) {
                        viewModel.fetchSubDistricts(
                            cityId = viewModel.selectedCity.value?.id ?: "",
                            selectedSubDistrictLabel = viewModel.initSelectedSubDistrictLabel
                        )
                    }
                }

                is EktpFormState.FetchedSubDistricts -> {
                    dismissLoadingDialog()
                    showSubDistrictsBottomsheet(it.subDistricts)
                }

                is EktpFormState.FetchedSubDistrictsAndSelect -> {
                    dismissLoadingDialog()

                    if (viewModel.selectedSubDistrict.value?.id != null && viewModel.initSelectedWardLabel != null) {
                        viewModel.fetchWards(
                            subDistrictId = viewModel.selectedSubDistrict.value?.id ?: "",
                            selectedWardLabel = viewModel.initSelectedWardLabel
                        )
                    }
                }

                is EktpFormState.FetchedWards -> {
                    dismissLoadingDialog()
                    showWardsBottomsheet(it.wards)
                }

                is EktpFormState.FetchedWardAndSelect -> {
                    dismissLoadingDialog()
                }

                is EktpFormState.LOADING -> {
                    showLoadingDialog()
                }

                is EktpFormState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBebasBottomsheet(it.exception)
                }

                else -> {}
            }
        }
    }

    private fun initAction() {
        binding.ddBirthdate.setOnClickListener {
            val text = if (!binding.ddBirthdate.getText()
                    .isNullOrEmpty()
            ) binding.ddBirthdate.getText() else null
            showPickBirthDateBottomsheet(text)
        }

        binding.ddGender.setOnClickListener {
            showGendersBottomsheet()
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
                viewModel.fetchCities(viewModel.selectedProvince.value?.id!!)
            }
        }

        binding.ddSubdistrict.setOnClickListener {
            if (viewModel.subDistricts != null) {
                showSubDistrictsBottomsheet(viewModel.subDistricts!!)
            } else if (viewModel.selectedCity.value != null) {
                viewModel.fetchSubDistricts(viewModel.selectedCity.value?.id!!)
            }
        }

        binding.ddWard.setOnClickListener {
            if (viewModel.wards != null) {
                showWardsBottomsheet(viewModel.wards!!)
            } else if (viewModel.selectedSubDistrict.value != null) {
                viewModel.fetchWards(viewModel.selectedSubDistrict.value?.id!!)
            }
        }

        binding.btnNext.setOnClickListener {
//            viewModel.setAddress()
        }
    }

    private var datePickerDialog: DatePickerDialog? = null

    private fun dismissDatePickerDialog() {
        datePickerDialog?.dismiss()
        datePickerDialog = null
    }

    private fun showPickBirthDateBottomsheet(text: String? = null) {
        dismissDatePickerDialog()

        datePickerDialog = DatePickerDialog(text)
        datePickerDialog?.setCallback(object : DatePickerDialog.Callback {
            override fun onClicked(year: Int, month: Int, dayOfMonth: Int, date: Date) {
                datePickerDialog?.dismiss()
                binding.ddBirthdate.setText(date.formatToEktpForm())
                Log.d("BebasLogger", "BIRTHDATE: ${date.time}")
                viewModel.setBirthDateState(date.time)
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

    private fun showGendersBottomsheet() {
        dismissPickerBottomsheet()
        pickerBottomsheet =
            BebasPickerBottomsheet(
                editTextHint = "Jenis Kelamin",
                content = BebasShared.genderItems,
                callback = object : BebasPickerBottomsheetAdapter.Callback {
                    override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                        dismissPickerBottomsheet()
                        binding.ddGender.setText(model.label)
                    }
                })
        showBottomsheet()
    }

    private fun showProvincesBottomsheet(provinces: List<BebasItemPickerBottomsheetModel>) {
        dismissPickerBottomsheet()

        pickerBottomsheet =
            BebasPickerBottomsheet(
                editTextHint = "Provinsi",
                content = provinces,
                callback = object : BebasPickerBottomsheetAdapter.Callback {
                    override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                        dismissPickerBottomsheet()
                        viewModel.selectProvince(model)
                    }
                })
        showBottomsheet()
    }

    private fun showCitiesBottomsheet(cities: List<BebasItemPickerBottomsheetModel>) {
        dismissPickerBottomsheet()

        pickerBottomsheet =
            BebasPickerBottomsheet(
                editTextHint = "Kota/Kabupaten",
                content = cities,
                callback = object : BebasPickerBottomsheetAdapter.Callback {
                    override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                        dismissPickerBottomsheet()
                        viewModel.selectCity(model)
                    }
                })
        showBottomsheet()
    }

    private fun showSubDistrictsBottomsheet(subDistricts: List<BebasItemPickerBottomsheetModel>) {
        dismissPickerBottomsheet()

        pickerBottomsheet =
            BebasPickerBottomsheet(
                editTextHint = "Kecamatan",
                content = subDistricts,
                callback = object : BebasPickerBottomsheetAdapter.Callback {
                    override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                        dismissPickerBottomsheet()
                        viewModel.selectSubDistrict(model)
                    }
                })
        showBottomsheet()
    }

    private fun showWardsBottomsheet(wards: List<BebasItemPickerBottomsheetModel>) {
        dismissPickerBottomsheet()

        pickerBottomsheet =
            BebasPickerBottomsheet(
                editTextHint = "Kelurahan",
                content = wards,
                callback = object : BebasPickerBottomsheetAdapter.Callback {
                    override fun onItemClicked(model: BebasItemPickerBottomsheetModel) {
                        dismissPickerBottomsheet()
                        viewModel.selectWard(model)
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