package com.fadlurahmanf.bebas_ui.bottomsheet

import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetFailedBinding

class FailedBottomsheet : BaseBottomsheet<BottomsheetFailedBinding>(
    BottomsheetFailedBinding::inflate
) {

    private var callback: Callback? = null
    private var failedImageDrawble: Drawable? = null

    companion object {
        const val IS_DIALOG_CANCELABLE = "IS_DIALOG_CANCELABLE"
        const val TITLE_TEXT = "TITLE_TEXT"
        const val MESSAGE_TEXT = "MESSAGE_TEXT"
        const val TRACE_ID_TEXT = "TRACE_ID_TEXT"
        const val BUTTON_TEXT = "BUTTON_TEXT"
    }

    override fun setup() {
        isCancelable = arguments?.getBoolean(IS_DIALOG_CANCELABLE, true) ?: true

        if (failedImageDrawble != null) {
            binding.ivAsset.setImageDrawable(failedImageDrawble)
        }

        binding.tvTitle.text = arguments?.getString(TITLE_TEXT)
        binding.tvDesc.text = arguments?.getString(MESSAGE_TEXT)
        binding.btnBottomsheet.setButtonText(arguments?.getString(BUTTON_TEXT) ?: "-")

        val traceId = arguments?.getString(TRACE_ID_TEXT, null)
        if (traceId != null) {
            binding.tvXrequestId.text = Html.fromHtml("Trace ID: <u><i>$traceId</i></u>")
            binding.tvXrequestId.visibility = View.VISIBLE
        }

        dialog?.setCanceledOnTouchOutside(false)

        binding.btnBottomsheet.setOnClickListener {
            if (callback != null) {
                callback?.onButtonClicked()
            } else if (isCancelable) {
                dismiss()
            }
        }
    }

    fun setImageDrawable(drawable: Drawable?) {
        if (drawable != null) {
            this.failedImageDrawble = drawable
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onButtonClicked()
    }
}