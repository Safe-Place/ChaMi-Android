package com.mbahgojol.chami.response

import com.google.gson.annotations.SerializedName

data class SignupResponse (
    @field:SerializedName("data")
    val data: List<DataUser>,

    @field:SerializedName("messages")
    val message : String,

    @field:SerializedName("status")
    val status : Boolean
)

data class DataUser(
    @field:SerializedName("divisi")
    val divisi: String?,

    @field:SerializedName("email")
    val email: String?,

    @field:SerializedName("id_pegawai")
    val id_pegawai: String?,

    @field:SerializedName("image_path")
    val image_path: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("password")
    val password: String?,

    @field:SerializedName("posisi")
    val posisi: String?,
)