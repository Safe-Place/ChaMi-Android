package com.mbahgojol.chami.ui.main.chat.group.converse

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.PayloadNotif
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.data.remote.NotifService
import com.mbahgojol.chami.utils.AppConstant
import com.mbahgojol.chami.utils.BaseViewModel
import com.mbahgojol.chami.utils.observableIo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val notifService: NotifService,
    private val service: FirestoreService,
    private val sharedPref: SharedPref
) : BaseViewModel() {

    val userList = MutableLiveData<List<Users>>()

    fun sendNotif(payloadNotif: PayloadNotif) {
        Observable.fromArray(userList.value)
            .flatMapIterable { it }
            .observableIo()
            .flatMap {
                payloadNotif.to = it.token
                payloadNotif.data?.idReceiver = it.user_id
                notifService.pushNotif(AppConstant.SERVER_KEY, payloadNotif)
                    .toObservable()
            }
            .subscribe()
            .autoDispose()
    }

    fun getAllUser(participants: List<String>?) {
        participants?.let { list ->
            Observable.fromArray(list)
                .observableIo()
                .flatMapIterable { it }
                .flatMap(this::getUser)
                .toList()
                .subscribe({
                    userList.value = it
                }, {

                })
                .autoDispose()
        }
    }

    private fun getUser(userId: String) = Observable.create<Users> { ob ->
        service.getUserProfile(userId)
            .get()
            .addOnSuccessListener {
                val model = it.toObject<Users>()
                ob.onNext(model)
            }.addOnFailureListener {
                ob.onError(it)
            }.addOnCompleteListener {
                ob.onComplete()
            }
    }
}