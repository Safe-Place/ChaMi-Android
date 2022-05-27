package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class ListChatModel {
    val user_id: String = ""
    val history_chat: MutableList<HistoryChatModel> = mutableListOf()
}

@Parcelize
data class HistoryChatModel(
    val isread: String = "",
    val roomid: String = "",
    val jabatan: String = "",
    val last_chat: String = "",
    val last_date: String = "",
    val profile_url: String = "",
    val user_id: String = "",
    val username: String = "",
    val isonline: Boolean = false
) : Parcelable