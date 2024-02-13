package com.fadlurahmanf.mapp_example.presentation

import android.content.Intent
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity
import com.fadlurahmanf.mapp_shared.rxbus.RxBus
import com.fadlurahmanf.mapp_shared.rxbus.RxEvent
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseExampleActivityAfterLogin<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseExampleActivity<VB>(inflater) {
    override fun onUserInteraction() {
        super.onUserInteraction()
        RxBus.publish(RxEvent.CheckUserTokenExpiresAt())
    }

    private val compositeDisposable = CompositeDisposable()

    private fun listenForceLogout() {
        compositeDisposable.add(RxBus.listen(RxEvent.ForceLogout::class.java).subscribe {
            val intent = Intent(this, ExampleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        })
    }

    override fun setup() {
        listenForceLogout()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}