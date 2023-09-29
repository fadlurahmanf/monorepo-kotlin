package com.fadlurahmanf.mapp_notification.domain.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class MappNotificationPlayerService : Service() {
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator

    companion object {
        const val START_INCOMING_CALL_PLAYER =
            "com.fadlurahmanf.mapp_notification.START_INCOMING_CALL_PLAYER"
        const val STOP_INCOMING_CALL_PLAYER =
            "com.fadlurahmanf.mapp_notification.STOP_INCOMING_CALL_PLAYER"

        fun startIncomingCallNotificationPlayer(context: Context) {
            val intent = Intent(context, MappNotificationPlayerService::class.java)
            intent.apply {
                action = START_INCOMING_CALL_PLAYER
            }
            context.startService(intent)
        }

        fun stopIncomingCallNotificationPlayer(context: Context) {
            val intent = Intent(context, MappNotificationPlayerService::class.java)
            intent.apply {
                action = STOP_INCOMING_CALL_PLAYER
            }
            context.stopService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MappLogger", "MappNotificationPlayerService ${intent?.action}")
        when (intent?.action) {
            START_INCOMING_CALL_PLAYER -> {
                releaseMediaPlayer()
                startMediaPlayer()
                listenRingerMode()
            }

            STOP_INCOMING_CALL_PLAYER -> {

            }
        }
        return START_STICKY
    }

    private var mediaPlayer: MediaPlayer? = null
    private fun releaseMediaPlayer() {
        removeListenerRingerMode()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator.cancel()
    }

    private fun startMediaPlayer() {
        when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> {
                val notifUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(applicationContext, notifUri)
                mediaPlayer?.prepare()
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()

                // effect vibrate
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createWaveform(
                            longArrayOf(0L, 2000L, 2000L),
                            0
                        )
                    )
                } else {
                    vibrator.vibrate(longArrayOf(0L, 2000L, 2000L), 0)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private var ringerModeListener: AudioManager.OnModeChangedListener =
        (AudioManager.OnModeChangedListener {
            Log.d("MappLogger", "RINGER MODE -> $it")
        })
    private var isListenRingerMode: Boolean = false

    private fun listenRingerMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isListenRingerMode) {
            audioManager.addOnModeChangedListener(
                ContextCompat.getMainExecutor(applicationContext),
                ringerModeListener
            )
            isListenRingerMode = true
        }
    }

    private fun removeListenerRingerMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isListenRingerMode) {
            audioManager.removeOnModeChangedListener(ringerModeListener)
        }
        isListenRingerMode = false
    }

    override fun onDestroy() {
        releaseMediaPlayer()
        super.onDestroy()
    }
}