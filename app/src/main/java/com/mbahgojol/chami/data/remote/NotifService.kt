package com.mbahgojol.chami.data.remote

import com.mbahgojol.chami.data.model.PayloadNotif
import com.mbahgojol.chami.utils.AppConstant.SEND_NOTIF
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotifService {
    @POST(SEND_NOTIF)
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun pushNotif(
        @Header("Authorization") token: String,
        @Body payloadNotif: PayloadNotif
    ): Single<ResponsePushNotif>
}