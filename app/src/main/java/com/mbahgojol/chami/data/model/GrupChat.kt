package com.mbahgojol.chami.data.model

data class GrupChat(
    var adminId: String = "",
    var namaGroup: String = "",
    var imgGroup: String = "",
    var detail: String = "",
    var lastChat: String = "",
    var lastAuthorId: String = "",
    var lastAuthor: String = "",
    var lastDate: String = "",
    var id: String = "",
    var participants: List<String> = mutableListOf()
)