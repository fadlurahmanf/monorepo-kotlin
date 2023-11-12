package com.fadlurahmanf.bebas_onboarding.presentation

import android.text.Editable
import android.text.TextWatcher
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityInputPhoneEmailBinding

class InputPhoneEmailActivity : BaseOnboardingActivity<ActivityInputPhoneEmailBinding>(ActivityInputPhoneEmailBinding::inflate) {
    override fun injectActivity() {

    }

    override fun setup() {
//        binding.etPhone.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (count > 0){
//                    binding.etPhone.setTextAppearance(this@InputPhoneEmailActivity, R.style.Font_Edittext)
//                }else{
//                    binding.etPhone.setTextAppearance(this@InputPhoneEmailActivity, R.style.Font_EdittextHint)
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//        })
    }

}