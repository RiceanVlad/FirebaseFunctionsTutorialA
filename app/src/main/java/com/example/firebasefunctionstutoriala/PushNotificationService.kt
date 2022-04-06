package com.example.firebasefunctionstutoriala

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*
import kotlin.random.Random

class PushNotificationService : FirebaseMessagingService() {

//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        val title = message.notification?.title
//        val body = message.notification?.body
//
//        val CHANNEL_ID = "HEADS_UP_NOTIFICATIONS"
//
//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            "MyNotification",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
//
//        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//        NotificationManagerCompat.from(this).notify(1, notification.build())
//    }

    fun getToken(): LiveData<String?> {
        val result = MutableLiveData<String?>()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get new FCM registration token
                result.value = task.result
            } else {
                result.value = null
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val CHANNEL_ID = "HEADS_UP_NOTIFICATIONS"

        val TAG = "TAG"
        Log.d(TAG, "From: ${remoteMessage.from}")

        val intent = Intent(this,MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            val notificationId = Random.nextInt()
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }
    }
}