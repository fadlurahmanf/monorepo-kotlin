package com.fadlurahmanf.bebas_ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.fadlurahmanf.bebas_ui.dialog.LoadingDialog
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

    fun showSnackBarLong(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
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
            RxBus.listen(RxEvent.ChangeLanguageEvent::class.java).subscribe {
                Log.d("BebasLogger", "change language: ${it.languageCode} & ${it.countryCode}")
                onChangeLanguageEvent(languageCode = it.languageCode, countryCode = it.countryCode)
            },
            RxBus.listen(RxEvent.ResetTimerForceLogout::class.java).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    Log.d("BebasLogger", "reset timer: ${it.expiresIn} & ${it.refreshExpiresIn}")
                    resetTimer(it.refreshExpiresIn)
                }
        )
    }

    private var forceLogoutTimer: CountDownTimer? = null

    fun resetTimer(refreshExpiresInSecond: Long) {
        forceLogoutTimer = object : CountDownTimer(refreshExpiresInSecond * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val inSecond = millisUntilFinished / 1000
                if (inSecond.toInt() % 10 == 0) {
                    Log.d("BebasLogger", "on tick $inSecond")
                }
            }

            override fun onFinish() {
                Log.d("BebasLogger", "on finish")
            }
        }
        forceLogoutTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseDisposable.clear()
    }
}