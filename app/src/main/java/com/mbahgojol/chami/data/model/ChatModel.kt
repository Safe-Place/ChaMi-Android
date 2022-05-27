package com.mbahgojol.chami.data.model

data class ChatModel(
    val roomid: String = "",
    val chatlog: MutableList<ChatLog> = mutableListOf(),
    val members: MutableList<Members> = mutableListOf()
)

data class ChatLog(
    val author_id: String = "",
    val author_name: String = "",
    val create_date: String = "",
    val type: Int = 0,
    val file_url: String = "",
    val id: String = "",
    val message: String = ""
)

data class Members(
    val user_id: String = "",
    val username: String = ""
)