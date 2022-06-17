package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Peserta(
    var challenges_id: String? = "",
    var nama_user: String? = "",
    var avatar_url : String? = "",
    var user_id: String? = "",
    var isWinner: Boolean? = false,
) : Parcelable