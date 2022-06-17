package com.mbahgojol.chami.ui.main.chat.personal.converse

import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.PayloadNotif
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.data.remote.NotifService
import com.mbahgojol.chami.utils.AppConstant
import com.mbahgojol.chami.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonalChatViewModel @Inject constructor(
    private val notifService: NotifService,
    private val service: FirestoreService,
    private val sharedPref: SharedPref
) :
    BaseViewModel() {

    fun sendNotif(payloadNotif: PayloadNotif) {
        notifService.pushNotif(AppConstant.SERVER_KEY, payloadNotif)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                service.incrementNotifPersonal(
                    payloadNotif.data?.idSender.toString(),
                    payloadNotif.data?.idReceiver.toString(),
                    payloadNotif.data?.roomId.toString()
                )
                Timber.e(it.toString())
            }, {
                Timber.e(it.message.toString())
            })
            .autoDispose()
    }
}