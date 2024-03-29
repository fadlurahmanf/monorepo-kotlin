package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.NotificationResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.UnreadNotificationCountResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface InboxApi {
    @GET("notification/v2/retail")
    fun getNotification(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("searchText") searchText: String? = null,
        @Query("status") status: String? = null,
    ): Single<BaseResponse<NotificationResponse>>

    @GET("notification/v2/retail/count/unread")
    fun getUnreadNotificationCount(): Observable<BaseResponse<UnreadNotificationCountResponse>>

    @PUT("notification/v2/read/{id}")
    fun updateReadNotification(
        @Path("id") notificationId: String,
        @Body request: JsonObject
    ): Observable<BaseResponse<Boolean>>
}