package com.mbahgojol.chami.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users constructor(
    var isonline: Boolean = false,
    var jabatan: String = "",
    var profile_url: String = "",
    var user_id: String = "",
    var username: String = "",
    var token: String = "",
    var chatRoom: ChatRoom = ChatRoom()
) : Parcelable


data class CreateUsers constructor(
    var isonline: Boolean = false,
    var jabatan: String = "",
    var profile_url: String = "",
    var user_id: String = "",
    var username: String = "",
    var token: String = ""
)