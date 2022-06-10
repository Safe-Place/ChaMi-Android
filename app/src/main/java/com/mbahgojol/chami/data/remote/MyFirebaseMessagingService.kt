package com.mbahgojol.chami.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.PayloadNotif
import com.mbahgojol.chami.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

private const val ID_REPEATING = 101

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
        Timber.e(msgId)
    }

    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.tag(TAG).d("From: %s", remoteMessage.from)

        remoteMessage.data.isNotEmpty().let {
            Timber.tag(TAG).d("Message data payload: %s", remoteMessage.data)
            val jsonData = Gson().toJson(remoteMessage.data)
            Timber.tag(TAG).d("RESPONSE %s", jsonData)

            sendBroadcast(Intent(this, MyBroadcast::class.java))

            if (jsonData != null) {
                val model = Gson().fromJson(jsonData, PayloadNotif.Data::class.java)

                if (sharedPref.userId != "") {
                    service.getAllCountNotif(sharedPref.userId)
                        .get()
                        .addOnSuccessListener {
                            val size = it.documents.size
                            sendNotification(
                                "(${if (size == 0) 1 else size}) Pesan Baru",
                                "Ketuk untuk melihat"
                            )
                        }.addOnFailureListener {
                            sendNotification(
                                "Pesan Baru",
                                "Ketuk untuk melihat"
                            )
                        }
                }
            }

            if (true) {
                scheduleJob()
            } else {
                handleNow()
            }
        }

        remoteMessage.notification?.let {
            Timber.tag(TAG).d("Message Notification Body: %s", it.body)
        }
    }

    override fun onNewToken(token: String) {
        Timber.tag(TAG).d("Refreshed token: %s", token)

        sendRegistrationToServer(token)
    }

    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
    }


    private fun handleNow() {
        Timber.tag(TAG).d("Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        Timber.tag(TAG).d("sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(vararg msg: String) {
        val channelId = "Channel_1"
        val channelName = "ChaMi Channel"

        val notificationManagerCompat =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(msg[0])
            .setContentText(msg[1])
            .setColor(ContextCompat.getColor(this, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setPriority(PRIORITY_MAX)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        notificationIntent.putExtra("notif", true)

        val intent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        builder.setAutoCancel(true)
        builder.setContentIntent(intent)

        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATING, notification)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}