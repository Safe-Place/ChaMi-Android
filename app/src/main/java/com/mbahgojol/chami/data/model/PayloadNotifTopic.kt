package com.mbahgojol.chami.data.model

data class PayloadNotifTopic(
    var message: Message
) {
    data class Message(
        var notification: Notification,
        var topic: String
    ) {
        data class Notification(
            var body: String,
            var title: String
        )
    }
}