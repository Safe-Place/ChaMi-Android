package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Files constructor(
    var nama_file: String? = "",
    var type: Long? = 5,
    var file_url: String? = "",
    var size_byte: String? = "",
    var create_at: String? = "",
    var author_id: String? = "",
    var file_id: String? = "",
    var author_div: String? = ""
): Parcelable
