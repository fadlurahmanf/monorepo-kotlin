package com.fadlurahmanf.mapp_example.presentation.crypto

import com.fadlurahmanf.mapp_example.databinding.ActivityRsaBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

class RsaActivity : BaseExampleActivity<ActivityRsaBinding>(ActivityRsaBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {

    }
}