package com.mbahgojol.chami.api

import com.mbahgojol.chami.response.SignupResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("users")
    fun signupUser(
        @Query("divisi") divisi: String,
        @Query("email") email: String,
        @Query("id_pegawai") id_pegawai: String,
        @Part("image_path") image_path: MultipartBody.Part,
        @Query("name") name: String,
        @Query("password") password: String,
        @Query("posisi") posisi: String
    ): Call<SignupResponse>
}