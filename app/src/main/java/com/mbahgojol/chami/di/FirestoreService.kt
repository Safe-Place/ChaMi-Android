package com.mbahgojol.chami.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mbahgojol.chami.data.model.*

class FirestoreService {
    private val db = Firebase.firestore

    fun addUser(users: CreateUsers, listen: (String) -> Unit) =
        db.collection("users")
            .add(users)
            .onSuccessTask { doc ->
                doc.update("user_id", doc.id)
                    .addOnSuccessListener {
                        listen(doc.id)
                    }
            }

    fun searchUser(username: String) =
        db.collection("users")
            .whereEqualTo("username", username)

    fun getAllUser() =
        db.collection("users")

    fun getChat(roomId: String) =
        db.collection("chat")
            .document(roomId)

    fun createRoom(roomId: String) =
        db.collection("chat")
            .document(roomId)
            .set(GetChatResponse(mutableListOf()))

    fun addChat(data: ChatLog, roomId: String) =
        db.collection("chat")
            .document(roomId)
            .update("chatlog", FieldValue.arrayUnion(data))

    fun getListChat(userId: String): CollectionReference =
        db.collection("users")
            .document(userId)
            .collection("chat_room")

    fun getRoomChat(userId: String, roomId: String) =
        db.collection("users")
            .document(userId)
            .collection("chat_room")
            .document(roomId)

    fun updateStatusInRoom(
        senderId: String, roomId: String,
        inRoom: Boolean
    ) = db.collection("users")
        .document(senderId)
        .collection("chat_room")
        .document(roomId)
        .update("inRoom", inRoom)

    fun getUserProfile(userId: String): DocumentReference =
        db.collection("users")
            .document(userId)

    fun getChatDetail(userId: String, roomId: String) =
        getUserProfile(userId)
            .collection("chat_room")
            .document(roomId)
            .collection("detail")
            .document(roomId)

    fun updateChatDetail(
        senderId: String,
        receiverId: String,
        roomId: String,
        detail: Detail
    ) {
        updateChatDetail(receiverId, roomId, detail)

        detail.isread = true
        getUserProfile(senderId)
            .collection("chat_room")
            .document(roomId)
            .collection("detail")
            .document(roomId)
            .set(detail, SetOptions.merge())
    }

    fun updateIsReadChat(
        userId: String,
        roomId: String,
        isread: Boolean
    ) {
        db.collection("users")
            .document(userId)
            .collection("chat_room")
            .document(roomId)
            .collection("detail")
            .document(roomId)
            .update("isread", isread)
    }

    fun updateChatList(
        senderId: String,
        receiverId: String,
        roomId: String,
        inRoom: Boolean
    ) {
        db.collection("users")
            .document(senderId)
            .collection("chat_room")
            .document(roomId)
            .set(ChatRoom(roomId, true, receiverId), SetOptions.merge())

        db.collection("users")
            .document(receiverId)
            .collection("chat_room")
            .document(roomId)
            .set(ChatRoom(roomId, inRoom, senderId), SetOptions.merge())
    }

    fun updateChatDetail(
        userId: String,
        roomId: String,
        detail: Detail
    ) {
        db.collection("users")
            .document(userId)
            .collection("chat_room")
            .document(roomId)
            .collection("detail")
            .document(roomId)
            .set(detail, SetOptions.merge())
    }

    fun pushNotif(
        userId: String,
        receiverId: String,
        status: Boolean
    ) {
        db.collection("notif")
            .document(userId)
            .collection("comein")
            .document(receiverId)
            .set(mapOf("status" to status), SetOptions.merge())
    }

    fun getNotif(userId: String) = db.collection("notif")
        .document(userId)
        .collection("comein")
        .whereEqualTo("status", true)

    fun deteleNotif(userId: String) {
        db.collection("notif")
            .document(userId)
            .delete()
    }
}