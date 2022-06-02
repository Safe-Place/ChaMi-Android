package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatLog(
    val author_id: String = "",
    val author_name: String = "",
    val create_date: String = "",
    val type: Int = 0,
    val file_url: String = "",
    val id: String = "",
    val message: String = "",
    val roomid: String = "",
    val isdelete: Boolean = false
) : Parcelable

data class GetChatResponse(val chatlog: MutableList<ChatLog> = mutableListOf())