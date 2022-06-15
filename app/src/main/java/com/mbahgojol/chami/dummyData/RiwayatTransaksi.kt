package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiwayatTransaksi(
    var tanggal: String,
    var nama: String,
    var harga: Int,
    var status: String,
) : Parcelable
