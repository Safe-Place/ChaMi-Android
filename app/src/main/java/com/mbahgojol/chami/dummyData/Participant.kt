package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Participant(
    var avatar: String,
    var nama: String,
    var namaFile: String,
    var pesan: String,
) : Parcelable