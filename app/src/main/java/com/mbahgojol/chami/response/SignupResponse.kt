package com.mbahgojol.chami.response

import com.google.gson.annotations.SerializedName

data class SignupResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status : String,

    @field:SerializedName("data")
    val data : DataUser
)

data class DataUser(
    @field:SerializedName("id_pegawai")
    val id_pegawai: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("posisi")
    val posisi: String,

    @field:SerializedName("divisi")
    val divisi: String,

//    @field:SerializedName("path")
//    val path: String?,

//    @field:SerializedName("registered_on")
//    val registered_on: String?,
)