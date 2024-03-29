package com.fadlurahmanf.mapp_ui.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_ui.R
import com.fadlurahmanf.mapp_ui.presentation.bottomsheet.InfoBottomsheet
import com.fadlurahmanf.mapp_ui.presentation.dialog.LoadingDialog
import com.fadlurahmanf.mapp_ui.presentation.dialog.LottieLoadingDialog

typealias MappInflateActivity<VB> = (LayoutInflater) -> VB

abstract class BaseMappActivity<VB : ViewBinding>(
    private val inflater: MappInflateActivity<VB>
) : AppCompatActivity() {

    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        initComponent()
        injectActivity()
        super.onCreate(savedInstanceState)
        bindingView()
        setup()
    }

    open fun bindingView() {
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun initComponent()

    abstract fun injectActivity()

    abstract fun setup()

    private var _isShowInfoBottomsheet: Boolean? = null
    private var _infoBottomsheet: InfoBottomsheet? = null
    fun showInfoBottomsheet(
        isCancelable: Boolean = true,
        @DrawableRes drawableRes: Int = R.drawable.il_happy,
        title: String,
        desc: String,
        buttonText: String
    ) {
        if (_isShowInfoBottomsheet == true) {
            dismissInfoBottomsheet()
        }
        _infoBottomsheet = InfoBottomsheet()
        _infoBottomsheet?.arguments = Bundle().apply {
            putBoolean(InfoBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
            putInt(InfoBottomsheet.DRAWABLE_RES, drawableRes)
            putString(InfoBottomsheet.TITLE_TEXT, title)
            putString(InfoBottomsheet.DESC_TEXT, desc)
            putString(InfoBottomsheet.BUTTON_TEXT, buttonText)
        }
        _infoBottomsheet?.show(supportFragmentManager, InfoBottomsheet::class.java.simpleName)
        _isShowInfoBottomsheet = true
    }

    fun showFailedBottomsheet(
        isCancelable: Boolean = true,
        @DrawableRes drawableRes: Int = R.drawable.il_sad,
        title: String,
        desc: String,
        buttonText: String
    ) {
        if (_isShowInfoBottomsheet == true) {
            dismissInfoBottomsheet()
        }
        _infoBottomsheet = InfoBottomsheet()
        _infoBottomsheet?.arguments = Bundle().apply {
            putBoolean(InfoBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
            putInt(InfoBottomsheet.DRAWABLE_RES, drawableRes)
            putString(InfoBottomsheet.TITLE_TEXT, title)
            putString(InfoBottomsheet.DESC_TEXT, desc)
            putString(InfoBottomsheet.BUTTON_TEXT, buttonText)
        }
        _infoBottomsheet?.show(supportFragmentManager, InfoBottomsheet::class.java.simpleName)
        _isShowInfoBottomsheet = true
    }

    private fun dismissInfoBottomsheet() {
        _infoBottomsheet?.dismiss()
        _isShowInfoBottomsheet = false
    }

    private var loadingDialog: LoadingDialog? = null
    private var lottieLoadingDialog: LottieLoadingDialog? = null
    fun showLoadingDialog(isCancelable: Boolean = false) {
        dismissLoadingDialog()
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(supportFragmentManager, LoadingDialog::class.java.simpleName)
        }
    }

    fun showLottieLoadingDialog(isCancelable: Boolean = false) {
        dismissLoadingDialog()
        if (lottieLoadingDialog == null) {
            lottieLoadingDialog = LottieLoadingDialog()
            lottieLoadingDialog?.show(supportFragmentManager, LottieLoadingDialog::class.java.simpleName)
        }
    }

    fun dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }

        if (lottieLoadingDialog != null){
            lottieLoadingDialog?.dismiss()
            lottieLoadingDialog = null
        }
    }
}