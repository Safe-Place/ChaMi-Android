package com.mbahgojol.chami.di

import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mbahgojol.chami.data.model.*

class FirestoreService {
    private val db = Firebase.firestore

    fun addUser(users: CreateUsers, id_user :String, listen: (String) -> Unit) =
        db.collection("users")
            .add(users)
            .onSuccessTask { doc ->
                doc.update("user_id", id_user)
                    .addOnSuccessListener {
                        listen(doc.id)
                    }
            }

    fun searchUser(id_user: String) =
        db.collection("users")
            .whereEqualTo("user_id", id_user)

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

    fun getListChatHome(userId: String) =
        db.collection("users")
            .document(userId)
            .collection("chat_room")
            .orderBy("last_update", Query.Direction.DESCENDING)

    fun addTime() = db.collection("test")
        .add(mapOf("createdAt" to FieldValue.serverTimestamp()))

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

    fun updateLastUpdateRoom(
        senderId: String,
        receiverId: String,
        roomId: String
    ) {
        db.collection("users")
            .document(senderId)
            .collection("chat_room")
            .document(roomId)
            .update("last_update", FieldValue.serverTimestamp())

        db.collection("users")
            .document(receiverId)
            .collection("chat_room")
            .document(roomId)
            .update("last_update", FieldValue.serverTimestamp())
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
            .set(
                mapOf(
                    "inRoom" to true,
                    "roomid" to roomId,
                    "receiver_id" to receiverId,
                    "last_update" to FieldValue.serverTimestamp()
                ), SetOptions.merge()
            )

        db.collection("users")
            .document(receiverId)
            .collection("chat_room")
            .document(roomId)
            .set(
                mapOf(
                    "inRoom" to inRoom,
                    "roomid" to roomId,
                    "receiver_id" to senderId,
                    "last_update" to FieldValue.serverTimestamp()
                ), SetOptions.merge()
            )
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

    fun updateToken(userId: String, token: String) =
        db.collection("users")
            .document(userId)
            .update("token", token)

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
    fun addFile(file: Files, listen: (String) -> Unit){
        db.collection("files")
            .add(file)
            .onSuccessTask { doc ->
                doc.update("file_id", doc.id)
                    .addOnSuccessListener {
                        listen(doc.id)
                    }
            }
    }

    fun getFiles(divisi:String): Query =
        db.collection("files")
            .whereEqualTo("author_div", divisi)

}