package com.mbahgojol.chami.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status : String,

    @field:SerializedName("token")
    val token : String,

    @field:SerializedName("data")
    val data : DataLogin
)

data class DataLogin(
    @field:SerializedName("id_pegawai")
    val id_pegawai: String,

    @field:SerializedName("password")
    val password: String,
)