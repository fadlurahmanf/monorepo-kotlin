package com.fadlurahmanf.bebas_main.presentation.home.bottomsheet

import android.app.Dialog
import com.fadlurahmanf.bebas_main.databinding.BottomsheetLogoutBinding
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet

class LogoutBottomsheet :
    BaseBottomsheet<BottomsheetLogoutBinding>(BottomsheetLogoutBinding::inflate) {
    private var callback: Callback? = null
    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun setup() {
        binding.btnCloseApp.setOnClickListener { view ->
            dialog?.let { itDialog ->
                callback?.onClosedAppClicked(itDialog)
            }
        }

        binding.btnLogoutApp.setOnClickListener { view ->
            dialog?.let { itDialog ->
                callback?.onLogoutAppClicked(itDialog)
            }
        }
    }

    interface Callback {
        fun onClosedAppClicked(dialog: Dialog)
        fun onLogoutAppClicked(dialog: Dialog)
    }
}