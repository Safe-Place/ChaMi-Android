package com.mbahgojol.chami.api

import com.mbahgojol.chami.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.sql.Struct
import javax.annotation.meta.TypeQualifierDefault

interface ApiService {
//    @Multipart
//    @POST("users/add")
//    fun signupUser(
//        @Part("id_pegawai") id_pegawai: RequestBody,
//        @Part("name") name: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part("email") email: RequestBody,
//        @Part("posisi") posisi: RequestBody,
//        @Part("divisi") devisi: RequestBody,
//        @Part image: MultipartBody.Part,
//    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("users/add")
    fun signupUser(
        @Field("id_pegawai") id_pegawai: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("posisi") posisi: String,
        @Field("divisi") devisi: String
    ): Call<SignupResponse>
}