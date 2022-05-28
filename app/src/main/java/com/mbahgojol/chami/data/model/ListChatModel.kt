package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ListChatModel(
    var user_id: String = "",
    var history_chat: MutableList<HistoryChatModel> = mutableListOf()
)

@Parcelize
data class HistoryChatModel(
    var isread: Boolean = false,
    var roomid: String = "",
    var jabatan: String = "",
    var last_chat: String = "",
    var last_date: String = "",
    var profile_url: String = "",
    var user_id: String = "",
    var username: String = "",
    var isonline: Boolean = false
) : Parcelable