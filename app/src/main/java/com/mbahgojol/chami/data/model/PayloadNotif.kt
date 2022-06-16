package com.mbahgojol.chami.data.model

data class PayloadNotif(
    val data: Data? = null,
    var to: String? = null
) {
    data class Data(
        var idReceiver: String? = null,
        val idSender: String? = null,
        val roomId: String? = null,
        val title: String? = null,
        val message: String? = null,
        val type: String? = null
    )
}