package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(
    var roomid: String = "",
    var inRoom: Boolean = false,
    var receiver_id: String = "",
    var istyping: Boolean = false
) : Parcelable

data class Detail(
    var last_chat: String = "",
    var last_date: String = "",
    var isread: Boolean = false
)