package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Validasi(
    var avatar: String,
    var nama: String,
    var divisi: String,
    var deskripsi: String,
    var id : String,
) : Parcelable