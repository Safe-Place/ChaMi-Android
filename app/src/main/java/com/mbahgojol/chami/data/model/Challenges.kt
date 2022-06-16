package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Challenges(
    var judul: String? = "",
    var deskripsi: String? = "",
    var point : String? = "",
    var due_date: Long? = 0,
    var challenge_id: String? = "",
    var ownerId: String? = "",
) : Parcelable
