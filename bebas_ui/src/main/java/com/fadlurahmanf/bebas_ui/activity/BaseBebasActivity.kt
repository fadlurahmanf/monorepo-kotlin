package com.fadlurahmanf.bebas_ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.exception.FulfillmentException
import com.fadlurahmanf.bebas_ui.R
import com.fadlurahmanf.bebas_ui.bottomsheet.FailedBottomsheet
import com.fadlurahmanf.bebas_ui.bottomsheet.ForceLogoutBottomsheet
import com.fadlurahmanf.bebas_ui.dialog.LoadingDialog
import com.fadlurahmanf.bebas_ui.font.BebasFontTypeSpan
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale

typealias BebasInflateActivity<VB> = (LayoutInflater) -> VB

abstract class BaseBebasActivity<VB : ViewBinding>(
    private val inflater: BebasInflateActivity<VB>
) : AppCompatActivity() {

    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        initComponent()
        injectActivity()
        super.onCreate(savedInstanceState)
        bindingView()
        initRxbusEvent()
        onBebasCreate(savedInstanceState)
    }

    open fun bindingView() {
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun initComponent()

    abstract fun injectActivity()

    abstract fun onBebasCreate(savedInstanceState: Bundle?)

    private var loadingDialog: LoadingDialog? = null
    fun showLoadingDialog(isCancelable: Boolean = false) {
        dismissLoadingDialog()
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(supportFragmentManager, LoadingDialog::class.java.simpleName)
        }
    }

    fun dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    fun showSnackBarShort(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    fun showSnackBarErrorLong(view: View, message: String, anchorView: View) {
        val ss = SpannableStringBuilder(message)
        ss.setSpan(
            BebasFontTypeSpan(
                "",
                ResourcesCompat.getFont(this, R.font.lexend_deca_regular)!!
            ), 0, message.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        val snackbar = Snackbar.make(view, ss, Snackbar.LENGTH_LONG)
            .setAnchorView(anchorView)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    fun showSnackBarSuccessLong(view: View, message: String, anchorView: View) {
        val ss = SpannableStringBuilder(message)
        ss.setSpan(
            BebasFontTypeSpan(
                "",
                ResourcesCompat.getFont(this, R.font.lexend_deca_regular)!!
            ), 0, message.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        val snackbar = Snackbar.make(view, ss, Snackbar.LENGTH_LONG)
            .setAnchorView(anchorView)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.green))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    private val baseDisposable = CompositeDisposable()

    open fun onChangeLanguageEvent(languageCode: String, countryCode: String) {
        val configuration = resources.configuration
        val local = Locale(languageCode, countryCode)
        Locale.setDefault(local)
        configuration.locale = local
        configuration.setLayoutDirection(local)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun initRxbusEvent() {
        baseDisposable.addAll(
            RxBus.listen(RxEvent.ChangeLanguageEvent::class.java).subscribe(
                {
                    Log.d("BebasLogger", "change language: ${it.languageCode} & ${it.countryCode}")
                    onChangeLanguageEvent(
                        languageCode = it.languageCode,
                        countryCode = it.countryCode
                    )
                },
                {},
                {}
            ),
            RxBus.listen(RxEvent.ResetTimerForceLogout::class.java).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        Log.d(
                            "BebasLogger",
                            "reset timer: ${it.expiresIn} & ${it.refreshExpiresIn}"
                        )
                        resetTimer(it.refreshExpiresIn)
                    },
                    {},
                    {}
                ),
            RxBus.listen(RxEvent.ForceLogoutBottomsheet::class.java).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        Log.d("BebasLogger", "force logout event")
                        showForceLogoutBottomsheet()
                    },
                    {},
                    {}
                )
        )
    }

    private var forceLogoutTimer: CountDownTimer? = null

    fun resetTimer(refreshExpiresInSecond: Long) {
        forceLogoutTimer?.cancel()
        forceLogoutTimer = null
        forceLogoutTimer = object : CountDownTimer(refreshExpiresInSecond * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val inSecond = millisUntilFinished / 1000
                if (inSecond.toInt() % 10 == 0) {
                    Log.d("BebasLogger", "on tick $inSecond")
                }
            }

            override fun onFinish() {
                Log.d("BebasLogger", "on finish")
                RxBus.publish(RxEvent.ForceLogoutBottomsheet)
            }
        }
        forceLogoutTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseDisposable.clear()
    }

    open fun goToAppPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    var forceLogoutBottomsheet: ForceLogoutBottomsheet? = null
    open fun showForceLogoutBottomsheet() {
        forceLogoutBottomsheet?.dismiss()
        forceLogoutBottomsheet = null
        forceLogoutBottomsheet = ForceLogoutBottomsheet()
        forceLogoutBottomsheet?.setCallback(object : ForceLogoutBottomsheet.Callback {
            override fun onButtonClicked() {
                forceLogoutBottomsheet?.dismiss()
                val intent = Intent(
                    this@BaseBebasActivity,
                    Class.forName("com.fadlurahmanf.bebas_onboarding.presentation.login.LoginActivity")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

        })

        forceLogoutBottomsheet?.show(
            supportFragmentManager,
            ForceLogoutBottomsheet::class.java.simpleName
        )
    }

    private var isFailedBottomsheetOpen: Boolean = false
    private var failedBottomsheet: FailedBottomsheet? = null
    open fun showFailedBebasBottomsheet(
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

    open fun showFailedBebasBottomsheet(
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
                exception.toProperTitle(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.TRACE_ID_TEXT,
                exception.xrequestId
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(this@BaseBebasActivity)
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

    open fun showFailedFulfillmentBottomsheet(
        exception: FulfillmentException,
        isCancelable: Boolean = true,
        callback: FailedBottomsheet.Callback? = null,
    ) {
        Log.e("BebasLogger", "FailedFulfillmentBottomsheet ${exception.toJson()}")
        if (isFailedBottomsheetOpen) {
            dismissFailedBottomsheet()
        }
        isFailedBottomsheetOpen = true
        val bundle = Bundle()
        bundle.apply {
            putString(
                FailedBottomsheet.TITLE_TEXT,
                exception.toProperTitle(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.TRACE_ID_TEXT,
                exception.xrequestId
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(this@BaseBebasActivity)
            )
            putBoolean(FailedBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
        }
        failedBottomsheet = FailedBottomsheet()
        failedBottomsheet?.setImageDrawable(exception.getDrawableImage(this))
        failedBottomsheet?.arguments = bundle
        if (callback != null) {
            failedBottomsheet?.setCallback(callback)
        }
        failedBottomsheet?.show(supportFragmentManager, FailedBottomsheet::class.java.simpleName)
    }

    open fun showForcedBackBottomsheet(
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
                exception.toProperTitle(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(this@BaseBebasActivity)
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

    open fun showForcedHomeBottomsheet(
        exception: BebasException,
        callback: FailedBottomsheet.Callback = object : FailedBottomsheet.Callback {
            override fun onButtonClicked() {
                dismissFailedBottomsheet()
                val intent = Intent(
                    this@BaseBebasActivity,
                    Class.forName("com.fadlurahmanf.bebas_main.presentation.home.HomeActivity")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
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
                exception.toProperTitle(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.MESSAGE_TEXT,
                exception.toProperMessage(this@BaseBebasActivity)
            )
            putString(
                FailedBottomsheet.BUTTON_TEXT,
                exception.toProperButtonText(this@BaseBebasActivity)
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

    open fun dismissFailedBottomsheet() {
        failedBottomsheet?.dismiss()
        failedBottomsheet = null
        isFailedBottomsheetOpen = false
    }
}