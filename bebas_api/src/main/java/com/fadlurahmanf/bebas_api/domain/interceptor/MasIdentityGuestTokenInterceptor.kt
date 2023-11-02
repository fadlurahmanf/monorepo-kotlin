package com.fadlurahmanf.bebas_api.domain.interceptor

//import com.fadlurahmanf.bebas_api.data.exception.BebasException
//import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
//import okhttp3.Interceptor
//import okhttp3.Request
//import okhttp3.Response
//
//class MasIdentityGuestTokenInterceptor(
//    private val mappLocalDatasource: MappLocalDatasource
//) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        return chain.proceed(getRequest(chain.request()))
//    }
//
//    private fun getRequest(request: Request): Request {
//        return mappLocalDatasource.getAll().map { entities ->
//            if (entities.isEmpty()) {
//                throw BebasException(rawMessage = "RC_ENTITY_IS_MISSING")
//            }
//            val entity = entities.first()
//            val accessToken = entity.guestToken
//            request.newBuilder().addHeader("Authorization", "Bearer $accessToken")
//                .build()
//        }.blockingGet()
//    }
//}