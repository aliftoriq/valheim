package com.example.solvr.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.solvr.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.net.toUri
import com.example.solvr.repository.AuthRepository
import retrofit2.Call

class FirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
        }

        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
        }

        val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title
        val body = remoteMessage.data["body"] ?: remoteMessage.notification?.body
        showNotification(title, body)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")

        val repository = AuthRepository()
        repository.saveFirebaseToken(token).enqueue(object : retrofit2.Callback<Any> {
            override fun onResponse(call: Call<Any>, response: retrofit2.Response<Any>) {
                if (response.isSuccessful) {
                    Log.d("FCM", "Token saved successfully")
                } else {
                    Log.e("FCM", "Token save failed: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("FCM", "Token save failed: ${t.message}", t)
            }
        })
    }


    private fun showNotification(title: String?, message: String?) {
        val channelId = "high_priority_channel"
        val channelName = "High Priority Channel"

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = "android.resource://${packageName}/raw/notifikasi".toUri()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(soundUri, audioAttributes)
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(com.example.solvr.R.drawable.icon_solver)
            .setContentTitle(title ?: "Notifikasi")
            .setContentText(message ?: "")
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH) //

        notificationManager.notify(0, notificationBuilder.build())
    }


}