package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiwayatChallenge(
    var judul: String,
    var deskripsi: String,
    var reward : Int,
    var due_date: String,
//    var is_winner: Boolean,
//    var winners: User,
) : Parcelable
