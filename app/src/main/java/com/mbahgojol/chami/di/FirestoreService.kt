package com.mbahgojol.chami.di

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.data.model.ChatModel
import com.mbahgojol.chami.data.model.HistoryChatModel
import com.mbahgojol.chami.data.model.ListChatModel

class FirestoreService {
    private val db = Firebase.firestore

    fun getChatByRoomId(roomId: String): DocumentReference =
        db.collection("chat")
            .document(roomId)

    fun createRoom(roomId: String): DocumentReference =
        db.collection("chat")
            .document(roomId)

    fun createRoomAutoGenerate(chatModel: ChatModel): Task<DocumentReference> =
        db.collection("chat")
            .add(chatModel)

    fun addChatByRoomId(roomId: String, data: ChatLog): Task<Void> =
        db.collection("chat")
            .document(roomId)
            .update("chatlog", FieldValue.arrayUnion(data))

    fun setListChatbyId(data: ListChatModel): Task<Void> =
        db.collection("list_chat")
            .document(data.user_id)
            .set(data)

    fun updateListHistoryChatbyId(data: HistoryChatModel): Task<Void> =
        db.collection("list_chat")
            .document(data.user_id)
            .update("history_chat", FieldValue.arrayRemove(data))

    fun getListChatById(userId: String): DocumentReference =
        db.collection("list_chat")
            .document(userId)
}