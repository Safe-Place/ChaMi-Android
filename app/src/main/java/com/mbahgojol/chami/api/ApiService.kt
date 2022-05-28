package com.mbahgojol.chami.api

import com.mbahgojol.chami.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("users")
    fun signupUser(
        @Part("divisi") divisi: RequestBody,
        @Part("email") email: RequestBody,
        @Part("id_pegawai") id_pegawai: RequestBody,
        @Part image_path: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("posisi") posisi: RequestBody,
    ): Call<SignupResponse>
}