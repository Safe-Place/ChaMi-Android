package com.mbahgojol.chami.dummyData

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var nama: String,
    var idPegawai: String,
    var email : String,
    var divisi: String,
    var posisi: String,
    var password: String,
    var avatar: Int,
) : Parcelable