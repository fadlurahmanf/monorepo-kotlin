package com.fadlurahmanf.bebas_api.domain.authenticator

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.data.api.IdentityGuestApi
import com.fadlurahmanf.bebas_api.data.dto.auth.RefreshUserTokenRequest
import com.fadlurahmanf.bebas_api.domain.interceptor.GuestTokenInterceptor
import com.fadlurahmanf.bebas_api.domain.interceptor.IdentityExceptionInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.fadlurahmanf.core_network.domain.network.CoreBaseNetworkProvider
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class UserTokenAuthenticator(
    context: Context
) : Authenticator, CoreBaseNetworkProvider(context, "BebasLoggerNetwork") {
    override fun authenticate(route: Route?, response: Response): Request {
        Log.d("BebasLoggerNetwork", "RETRY REQUEST BY REFRESH TOKEN")
        val refreshToken = BebasShared.getRefreshToken()
        val requestRefreshUserTokenRequest = RefreshUserTokenRequest(
            refreshToken = refreshToken
        )
        val refreshTokenResp =
            identityApi().refreshUserToken(requestRefreshUserTokenRequest).execute()
        val currentRequest = response.request
        return if (refreshTokenResp.isSuccessful) {
            Log.d("BebasLoggerNetwork", "RETRY REQUEST BY REFRESH TOKEN SUCCESS")
            val data = refreshTokenResp.body()?.data
            if (data?.accessToken != null && data.refreshToken != null) {
                BebasShared.setAccessToken(data.accessToken ?: "")
                BebasShared.setRefreshToken(data.refreshToken ?: "")
                RxBus.publish(
                    RxEvent.ResetTimerForceLogout(
                        expiresIn = data.expiresIn ?: (60L * 3),
                        refreshExpiresIn = data.refreshExpiresIn ?: (60L * 5)
                    )
                )
                currentRequest.newBuilder().removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${data.accessToken ?: "-"}")
                    .removeHeader("R-Refresh-Access-Token")
                    .addHeader("R-Refresh-Access-Token", "true")
                    .build()
            } else {
                currentRequest.newBuilder().build()
            }
        } else {
            Log.d("BebasLoggerNetwork", "RETRY REQUEST BY REFRESH TOKEN FAILED")
            currentRequest.newBuilder().build()
        }
    }

    private fun identityApi(): IdentityGuestApi {
        val okHttpClientBuilder =
            OkHttpClient.Builder().addInterceptor(IdentityExceptionInterceptor(context))
                .addInterceptor(GuestTokenInterceptor())
                .addInterceptor(bodyLoggingInterceptor())
                .addInterceptor(getChuckerInterceptor())
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS).build()
        return Retrofit.Builder().baseUrl("${BebasShared.getBebasUrl()}identity-service/")
            .client(okHttpClientBuilder)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(IdentityGuestApi::class.java)
    }


}