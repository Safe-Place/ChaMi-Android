package com.mbahgojol.chami.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.data.model.Detail
import com.mbahgojol.chami.data.model.GetChatResponse

class FirestoreService {
    private val db = Firebase.firestore

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

}