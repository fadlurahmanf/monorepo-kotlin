package com.fadlurahmanf.mapp_notification.domain.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

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
                startRinging()
                listenRingerMode()
            }

            STOP_INCOMING_CALL_PLAYER -> {

            }
        }
        return START_STICKY
    }

    private val ringerModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                AudioManager.RINGER_MODE_CHANGED_ACTION -> {
                    Log.d("MappLogger", "RINGER MODE: ${audioManager.ringerMode}")
                    if (currentRingerMode == audioManager.ringerMode){
                        Log.d("MappLogger", "PREVIOUS RINGER MODE = CURRENT RINGER MODE")
                        return
                    }
                    when(audioManager.ringerMode){
                        AudioManager.RINGER_MODE_NORMAL -> {
                            releaseMediaPlayer()
                            startRingingNormalMode()
                            listenRingerMode()
                        }
                        AudioManager.RINGER_MODE_VIBRATE -> {
                            releaseMediaPlayer()
                            startRingingVibrateMode()
                            listenRingerMode()
                        }
                        AudioManager.RINGER_MODE_SILENT -> {
                            releaseMediaPlayer()
                            startRingingSilentMode()
                            listenRingerMode()
                        }
                    }
                }
            }
        }
    }
    private var ringerModeReceiverRegistered:Boolean = false

    private fun listenRingerMode() {
        if (!ringerModeReceiverRegistered){
            registerReceiver(ringerModeReceiver, IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION))
        }
        ringerModeReceiverRegistered = true
    }

    private var mediaPlayer: MediaPlayer? = null
    private fun releaseMediaPlayer() {
        removeListenerRingerMode()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator.cancel()
    }

    private fun removeListenerRingerMode() {
        if (ringerModeReceiverRegistered){
            unregisterReceiver(ringerModeReceiver)
        }
        ringerModeReceiverRegistered = false
    }

    private var currentRingerMode:Int = AudioManager.RINGER_MODE_SILENT

    private fun startRinging() {
        when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> {
                startRingingNormalMode()

            }
            AudioManager.RINGER_MODE_VIBRATE -> {
                startRingingVibrateMode()

            }
            AudioManager.RINGER_MODE_SILENT -> {
                startRingingSilentMode()
            }
        }
    }

    private fun startRingingNormalMode() {
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

        currentRingerMode = AudioManager.RINGER_MODE_NORMAL
    }

    private fun startRingingVibrateMode() {
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

        currentRingerMode = AudioManager.RINGER_MODE_VIBRATE
    }

    private fun startRingingSilentMode() {
        currentRingerMode = AudioManager.RINGER_MODE_SILENT
    }

    override fun onDestroy() {
        releaseMediaPlayer()
        super.onDestroy()
    }
}