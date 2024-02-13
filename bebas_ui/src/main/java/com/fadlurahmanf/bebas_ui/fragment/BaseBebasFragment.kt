package com.fadlurahmanf.bebas_ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.R
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import com.fadlurahmanf.bebas_ui.dialog.LoadingDialog

typealias BebasInflateFragment<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB

abstract class BaseBebasFragment<VB : ViewBinding>(
    private val fragmentInflater: BebasInflateFragment<VB>
) : Fragment() {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = fragmentInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        injectFragment()
        onBebasCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBebasViewCreated(view, savedInstanceState)
    }

    abstract fun initComponent()

    abstract fun injectFragment()

    abstract fun onBebasCreate(savedInstanceState: Bundle?)

    abstract fun onBebasViewCreated(view: View, savedInstanceState: Bundle?)

    private var loadingDialog: LoadingDialog? = null
    open fun showLoadingDialog(isCancelable: Boolean = false) {
        dismissLoadingDialog()
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(requireActivity().supportFragmentManager, LoadingDialog::class.java.simpleName)
        }
    }

    open fun dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    private var isFailedBottomsheetOpen: Boolean = false
    private var failedBottomsheet: FailedBottomsheet? = null
    open fun showFailedBottomsheet(
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

    open fun showFailedBottomsheet(
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

    open fun showForcedBackBottomsheet(
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

    open fun dismissFailedBottomsheet() {
        failedBottomsheet?.dismiss()
        failedBottomsheet = null
        isFailedBottomsheetOpen = false
    }
}