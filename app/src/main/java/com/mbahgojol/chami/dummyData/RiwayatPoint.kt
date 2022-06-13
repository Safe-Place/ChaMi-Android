package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiwayatPoint(
    var jenis: String,
    var nama: String,
    var tanggal: String,
    var nominal: Int,
    var status: String
) : Parcelable
