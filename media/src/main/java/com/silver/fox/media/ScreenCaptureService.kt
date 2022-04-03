package com.silver.fox.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.util.Log

class ScreenCaptureService : Service() {

    companion object {
        private const val TAG = "ScreenCaptureService"

        private const val Notification_Channel_ID = "Railgun_Notification_ID"
        private const val Notification_Channel_Name = "Railgun_Notification_Name"

        private const val Notification_ID = 0x1000

        const val KEY_RESULT_CODE = "result_code"
        const val KEY_SCREEN_INTENT = "screen_intent"
        const val KEY_NOTIFICATION_ICON = "notification_icon"
        const val KEY_NOTIFICATION_TITLE = "notification_title"
        const val KEY_NOTIFICATION_DESC = "notification_desc"
    }

    private val bridge = ServiceBridge()

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "PlaybackCaptureService onCreate")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "PlaybackCaptureService onStartCommand")
        }
        try {
            val resultCode = intent?.getIntExtra(KEY_RESULT_CODE, 0) ?: 0
            val screenIntent = intent?.getParcelableExtra<Intent>(KEY_SCREEN_INTENT)
            val iconRes = intent?.getIntExtra(KEY_NOTIFICATION_ICON, 0) ?: 0 // resId
            val title = intent?.getStringExtra(KEY_NOTIFICATION_TITLE) ?: ""
            val desc = intent?.getStringExtra(KEY_NOTIFICATION_DESC) ?: ""

            val notification = createNotification(iconRes, title, desc)
            startForeground(Notification_ID, notification)
            screenIntent?.let {
                val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                val mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, it)
                bridge.setMediaProjection(mediaProjection)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "PlaybackCaptureService onBind")
        }
        return bridge
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "PlaybackCaptureService onUnbind")
        }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "PlaybackCaptureService onDestroy")
        }
        super.onDestroy()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(Notification_ID)
    }

    private fun createNotification(iconRes: Int, title: String, desc: String): Notification {
        return NotificationCompat.Builder(this, getNotificationChannelID(desc))
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(iconRes)
                .setAutoCancel(false)
                .build()
    }

    private fun getNotificationChannelID(desc: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(Notification_Channel_ID, Notification_Channel_Name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = desc
            manager.createNotificationChannel(channel)
        }
        return Notification_Channel_ID
    }

    class ServiceBridge : Binder() {

        private var mediaProjection: MediaProjection? = null

        internal fun setMediaProjection(projection: MediaProjection) {
            mediaProjection = projection
        }

        fun getMediaProject(): MediaProjection? {
            return mediaProjection
        }
    }
}