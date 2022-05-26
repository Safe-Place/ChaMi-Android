package com.mbahgojol.chami.di

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreService {
    private val db = Firebase.firestore

    fun listenChatById(userId: String): Query =
        db.collection("chat")
            .whereEqualTo("chat_id", userId)

    fun listenListChatById(userId: String): DocumentReference =
        db.collection("list_chat")
            .document(userId)
}