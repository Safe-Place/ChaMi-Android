package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Produk(
    var foto: String,
    var nama: String,
    var harga: Int,
    var deskripsi: String,
) : Parcelable