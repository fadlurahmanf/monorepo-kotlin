package com.fadlurahmanf.mapp_example.presentation.session

import com.fadlurahmanf.mapp_example.databinding.ActivityAfterLoginBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivityAfterLogin
import com.fadlurahmanf.mapp_example.presentation.session.view_model.AfterLoginViewModel
import javax.inject.Inject

class ActivityAfterLogin : BaseExampleActivityAfterLogin<ActivityAfterLoginBinding>(ActivityAfterLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: AfterLoginViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {

    }
}