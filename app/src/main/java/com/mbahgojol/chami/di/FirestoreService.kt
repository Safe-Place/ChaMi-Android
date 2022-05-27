package com.mbahgojol.chami.di

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.data.model.HistoryChatModel
import com.mbahgojol.chami.data.model.ListChatModel

class FirestoreService {
    private val db = Firebase.firestore

    fun listenChatByRoomId(roomId: String): DocumentReference =
        db.collection("chat")
            .document(roomId)

    fun addChatByRoomId(roomId: String, data: ChatLog): Task<Void> =
        db.collection("chat")
            .document(roomId)
            .update("chatlog", FieldValue.arrayUnion(data))

    fun setListChatbyId(userId: String, data: ListChatModel): Task<Void> =
        db.collection("list_chat")
            .document(userId)
            .set(data)

    fun updateListHistoryChatbyId(userId: String, data: HistoryChatModel): Task<Void> =
        db.collection("list_chat")
            .document(userId)
            .update("history_chat", FieldValue.arrayUnion(data))

    fun updateLastChatbyId(userId: String, lastChat: String): Task<Void> =
        db.collection("list_chat")
            .document(userId)
            .update("last_chat", lastChat)

    fun listenListChatById(userId: String): DocumentReference =
        db.collection("list_chat")
            .document(userId)
}