package com.mbahgojol.chami.data

class ListChatModel {
    val user_id: String = ""
    val history_chat: MutableList<HistoryChatModel> = mutableListOf()
}

class HistoryChatModel {
    val isread: String = ""
    val last_chat: String = ""
    val last_date: String = ""
    val profile_url: String = ""
    val user_id: String = ""
    val username: String = ""
    val isonline: Boolean = false
}