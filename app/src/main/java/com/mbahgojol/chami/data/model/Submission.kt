package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Submission(
    var url_file: String? = "",
    var nama_file: String? = "",
    var avatar_user: String?= "",
    var nama_user : String? = "",
    var user_id: String? = "",
    var pesan: String? = "",
    var submission_id : String? = "",
    var challenge_id: String? = "",
) : Parcelable