package com.fadlurahmanf.bebas_transaction.presentation

import android.os.Bundle
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_storage.DaggerBebasStorageComponent
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.domain.di.BebasTransactionComponent
import com.fadlurahmanf.bebas_transaction.domain.di.DaggerBebasTransactionComponent
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import com.fadlurahmanf.bebas_ui.fragment.BaseBebasFragment
import com.fadlurahmanf.bebas_ui.fragment.BebasInflateFragment
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseTransactionFragment<VB : ViewBinding>(inflate: BebasInflateFragment<VB>) :
    BaseBebasFragment<VB>(inflate) {

    lateinit var component: BebasTransactionComponent

    override fun initComponent() {
        val cryptoComponent = DaggerCoreCryptoComponent.factory().create()
        component = DaggerBebasTransactionComponent.factory()
            .create(
                requireActivity().applicationContext,
                cryptoComponent,
                DaggerCorePlatformComponent.factory().create(),
                DaggerBebasStorageComponent.factory()
                    .create(requireActivity().applicationContext, cryptoComponent)
            )
    }

    fun logConsole() = (requireActivity().applicationContext as BebasApplication).logConsole


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
        failedBottomsheet?.show(
            requireActivity().supportFragmentManager,
            FailedBottomsheet::class.java.simpleName
        )
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
                exception.toProperTitle(requireContext())
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(requireContext())
            )
            putString(
                FailedBottomsheet.TRACE_ID_TEXT,
                exception.xrequestId
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(requireContext())
            )
            putBoolean(FailedBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
        }
        failedBottomsheet = FailedBottomsheet()
        failedBottomsheet?.arguments = bundle
        if (callback != null) {
            failedBottomsheet?.setCallback(callback)
        }
        failedBottomsheet?.show(
            requireActivity().supportFragmentManager,
            FailedBottomsheet::class.java.simpleName
        )
    }

    fun showForcedBackBottomsheet(
        exception: BebasException,
        callback: FailedBottomsheet.Callback = object : FailedBottomsheet.Callback {
            override fun onButtonClicked() {
                dismissFailedBottomsheet()
                requireActivity().finish()
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
                exception.toProperTitle(requireContext())
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(requireContext())
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(requireContext())
            )
            putBoolean(FailedBottomsheet.IS_DIALOG_CANCELABLE, false)
        }
        failedBottomsheet = FailedBottomsheet()
        failedBottomsheet?.arguments = bundle
        if (callback != null) {
            failedBottomsheet?.setCallback(callback)
        }
        failedBottomsheet?.show(
            requireActivity().supportFragmentManager,
            FailedBottomsheet::class.java.simpleName
        )
    }

    fun dismissFailedBottomsheet() {
        failedBottomsheet?.dismiss()
        failedBottomsheet = null
        isFailedBottomsheetOpen = false
    }
}