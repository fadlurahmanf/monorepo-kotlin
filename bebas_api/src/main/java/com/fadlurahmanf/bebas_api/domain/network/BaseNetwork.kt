package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fadlurahmanf.bebas_api.domain.interceptor.CustomLoggingInterceptor
import com.fadlurahmanf.bebas_api.domain.interceptor.MappExceptionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseNetwork<T>(var context: Context) {

    var service: T? = null

    private fun bodyLoggingInterceptor(): CustomLoggingInterceptor {
        return CustomLoggingInterceptor("BebasNetworkLogger").setLevel(CustomLoggingInterceptor.Level.BODY)
    }

    private fun getChuckerInterceptor(): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
        return ChuckerInterceptor.Builder(context).collector(collector)
            .maxContentLength(Long.MAX_VALUE)
            .alwaysReadResponseBody(false)
            .build()
    }

    open fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.addInterceptor(bodyLoggingInterceptor())
            .addInterceptor(getChuckerInterceptor())
            .addInterceptor(MappExceptionInterceptor(context))
    }

    private fun provideClient(timeOut: Long): OkHttpClient {
        return okHttpClientBuilder(OkHttpClient.Builder())
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .build()
    }


    private fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    private fun provideRetrofit(timeOut: Long): Retrofit {
        return providesRetrofitBuilder().baseUrl(getBaseUrl())
            .client(provideClient(timeOut))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    open fun networkService(timeOut: Long): T {
        val retrofit = provideRetrofit(timeOut)
        service = retrofit.create(getApi())
        return service!!
    }

    abstract fun getApi(): Class<T>

    abstract fun getBaseUrl(): String
}