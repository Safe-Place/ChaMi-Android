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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val notifService: NotifService,
    private val service: FirestoreService,
    private val sharedPref: SharedPref
) : BaseViewModel() {

    val userList = MutableLiveData<List<Users>>()

    fun sendNotif(payloadNotif: PayloadNotif) {
        if (userList.value != null)
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
                    Timber.e(it)
                })
                .autoDispose()
        }
    }

    fun addAllCountNotif(
        participants: List<String>?,
        groupId: String
    ) {
        participants?.let { list ->
            Observable.fromArray(list)
                .observableIo()
                .flatMapIterable { it }
                .flatMap {
                    val isRead = it == sharedPref.userId
                    addCountNotif(groupId, it, isRead)
                }
                .subscribe({

                }, {
                    Timber.e(it)
                })
                .autoDispose()
        }
    }

    private fun getUser(userId: String) = Observable.create<Users> { ob ->
        service.getUserProfile(userId)
            .get()
            .addOnSuccessListener {
                if (it != null && it.exists()) {
                    val model = it.toObject<Users>()
                    ob.onNext(model)
                }
            }.addOnFailureListener {
                ob.onError(it)
            }.addOnCompleteListener {
                ob.onComplete()
            }
    }

    private fun addCountNotif(groupId: String, userId: String, isRead: Boolean) =
        Observable.create<String> { ob ->
            service.addNotifCountGrup(groupId, userId, isRead)
                .addOnSuccessListener {
                    ob.onNext("Success")
                }.addOnFailureListener {
                    if (!it.message.toString().contains("NOT_FOUND")) {
                        ob.onError(it)
                    }
                }.addOnCompleteListener {
                    ob.onComplete()
                }
        }
}