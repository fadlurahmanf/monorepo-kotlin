package com.fadlurahmanf.mapp_config.helper.di

import android.content.Context
import com.fadlurahmanf.mapp_config.MappComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider

object CoreInjectHelper {
    fun provideMappComponent(applicationContext: Context): MappComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideMappComponent()
        } else {
            throw IllegalStateException("application context should be IMappComponentProvider")
        }
    }
}