package com.mbahgojol.chami.api

import com.mbahgojol.chami.response.LoginResponse
import com.mbahgojol.chami.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.sql.Struct
import javax.annotation.meta.TypeQualifierDefault

interface ApiService {

    @FormUrlEncoded
    @POST("api/v1/register")
    fun signupUser(
        @Field("id_pegawai") id_pegawai: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("password2") password2: String,
        @Field("email") email: String,
        @Field("posisi") posisi: String,
        @Field("divisi") devisi: String
    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("api/v1/login")
    fun loginUser(
        @Field("id_pegawai") id_pegawai: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
}