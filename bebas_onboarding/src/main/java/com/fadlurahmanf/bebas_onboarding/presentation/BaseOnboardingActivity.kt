package com.fadlurahmanf.bebas_onboarding.presentation

import android.os.Bundle
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.domain.di.BebasOnboardingComponent
import com.fadlurahmanf.bebas_onboarding.domain.di.DaggerBebasOnboardingComponent
import com.fadlurahmanf.bebas_storage.DaggerBebasStorageComponent
import com.fadlurahmanf.bebas_ui.activity.BaseBebasActivity
import com.fadlurahmanf.bebas_ui.activity.BebasInflateActivity
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseOnboardingActivity<VB : ViewBinding>(inflate: BebasInflateActivity<VB>) :
    BaseBebasActivity<VB>(inflate) {

    lateinit var component: BebasOnboardingComponent
    override fun initComponent() {
        val cryptoComponent = DaggerCoreCryptoComponent.factory().create()
        component = DaggerBebasOnboardingComponent.factory()
            .create(
                applicationContext,
                cryptoComponent,
                DaggerCorePlatformComponent.factory().create(),
                DaggerBebasStorageComponent.factory().create(applicationContext, cryptoComponent)
            )
    }

    fun logConsole() = (applicationContext as BebasApplication).logConsole


    private var isFailedBottomsheetOpen: Boolean = false
    private var failedBottomsheet: FailedBottomsheet? = null
    fun showFailedBottomsheet(
        title: String,
        message: String,
        buttonText: String? = null,
        callback: FailedBottomsheet.Callback? = null
    ) {
        if (isFailedBottomsheetOpen) {
            dismissFailedBottomsheet()
        }
        isFailedBottomsheetOpen = true
        val bundle = Bundle()
        bundle.apply {
            putString(FailedBottomsheet.TITLE_TEXT, title)
            putString(FailedBottomsheet.MESSAGE_TEXT, message)
            if (buttonText != null) {
                putString(FailedBottomsheet.BUTTON_TEXT, buttonText)
            } else {
                putString(FailedBottomsheet.BUTTON_TEXT, getString(R.string.ok))
            }
        }
        failedBottomsheet = FailedBottomsheet()
        failedBottomsheet?.arguments = bundle
        if (callback != null) {
            failedBottomsheet?.setCallback(callback)
        }
        failedBottomsheet?.show(supportFragmentManager, FailedBottomsheet::class.java.simpleName)
    }

    fun showFailedBottomsheet(
        exception: BebasException,
        isCancelable: Boolean = true,
        callback: FailedBottomsheet.Callback? = null,
    ) {
        Log.e("BebasLogger", "FailedBottomsheet ${exception.toJson()}")
        if (isFailedBottomsheetOpen) {
            dismissFailedBottomsheet()
        }
        isFailedBottomsheetOpen = true
        val bundle = Bundle()
        bundle.apply {
            putString(
                FailedBottomsheet.TITLE_TEXT,
                exception.toProperTitle(this@BaseOnboardingActivity)
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(this@BaseOnboardingActivity)
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(this@BaseOnboardingActivity)
            )
            putBoolean(FailedBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
        }
        failedBottomsheet = FailedBottomsheet()
        failedBottomsheet?.arguments = bundle
        if (callback != null) {
            failedBottomsheet?.setCallback(callback)
        }
        failedBottomsheet?.show(supportFragmentManager, FailedBottomsheet::class.java.simpleName)
    }

    fun showForcedBackBottomsheet(
        exception: BebasException,
        callback: FailedBottomsheet.Callback = object : FailedBottomsheet.Callback {
            override fun onButtonClicked() {
                dismissFailedBottomsheet()
                finish()
            }
        },
    ) {
        Log.e("BebasLogger", "ForcedBackBottomsheet ${exception.rawMessage}")
        if (isFailedBottomsheetOpen) {
            dismissFailedBottomsheet()
        }
        isFailedBottomsheetOpen = true
        val bundle = Bundle()
        bundle.apply {
            putString(
                FailedBottomsheet.TITLE_TEXT,
                exception.toProperTitle(this@BaseOnboardingActivity)
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(this@BaseOnboardingActivity)
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(this@BaseOnboardingActivity)
            )
            putBoolean(FailedBottomsheet.IS_DIALOG_CANCELABLE, false)
        }
        failedBottomsheet = FailedBottomsheet()
        failedBottomsheet?.arguments = bundle
        if (callback != null) {
            failedBottomsheet?.setCallback(callback)
        }
        failedBottomsheet?.show(supportFragmentManager, FailedBottomsheet::class.java.simpleName)
    }

    fun dismissFailedBottomsheet() {
        failedBottomsheet?.dismiss()
        failedBottomsheet = null
        isFailedBottomsheetOpen = false
    }
}