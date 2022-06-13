package com.mbahgojol.chami.data.model

data class PayloadNotif(
    val data: Data? = null,
    val to: String? = null
) {
    data class Data(
        val idReceiver: String? = null,
        val idSender: String? = null,
        val roomId: String? = null,
        val title: String? = null,
        val message: String? = null,
        val type: String? = null
    )
}