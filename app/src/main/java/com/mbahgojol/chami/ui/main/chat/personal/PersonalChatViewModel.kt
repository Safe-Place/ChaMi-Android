package com.mbahgojol.chami.ui.main.chat.personal

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.data.model.ChatRoom
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.di.FirestoreService
import com.mbahgojol.chami.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonalChatViewModel @Inject constructor(private val service: FirestoreService) :
    BaseViewModel() {

    val listenList = MutableLiveData<MutableList<Users>>()

    fun collectUser(snapshot: QuerySnapshot?) {
        Observable.fromArray(snapshot?.documents)
            .flatMapIterable { it }
            .flatMap {
                val model = it.toObject<ChatRoom>()
                getUser(model)
            }
            .toList()
            .subscribe({
                listenList.value = it
            }, {
                Timber.e(it.message.toString())
            })
            .autoDispose()
    }

    private fun getUser(chatRoom: ChatRoom?) = Observable.create<Users> { emitter ->
        service.getUserProfile(chatRoom?.receiver_id ?: "")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject<Users>()
                    user?.chatRoom = chatRoom ?: ChatRoom()
                    emitter.onNext(user)
                } else {
                    emitter.onError(e)
                    Timber.e("Tidak ada List Chat")
                }

                emitter.onComplete()
            }
    }
}