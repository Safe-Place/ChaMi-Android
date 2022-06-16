package com.mbahgojol.chami.dummyData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryChallenge(
    var judul: String,
    var deskripsi: String,
    var reward : Int,
    var due_date: String,
    var status: String,
) : Parcelable
