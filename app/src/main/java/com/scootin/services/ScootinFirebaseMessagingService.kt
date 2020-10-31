package com.scootin.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class ScootinFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Timber.i("Firebase From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Timber.i("Firebase Message data payload: ${remoteMessage.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
                sendNotification(remoteMessage.data.toString())
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.i("Message Notification Body: ${it.body}")
            sendNotification(it.body.toString())
        }

    }

    override fun onNewToken(token: String) {
        Timber.i("Firebase Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun scheduleJob() {
    }

    private fun handleNow() {
        Timber.i("Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Timber.i("sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(messageBody: String) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_stat_ic_notification)
//            .setContentTitle(getString(R.string.fcm_message))
//            .setContentText(messageBody)
//            .setAutoCancel(true)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Channel human readable title",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}