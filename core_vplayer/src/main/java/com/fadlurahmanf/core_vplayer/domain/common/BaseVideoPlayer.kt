package com.fadlurahmanf.core_vplayer.domain.common

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector

abstract class BaseVideoPlayer(context: Context) {

    var audioManager: AudioManager
    var handler: Handler
    var exoPlayer: ExoPlayer
    private var trackSelector: TrackSelector =
        DefaultTrackSelector(context, AdaptiveTrackSelection.Factory())

    var currentState = Player.STATE_IDLE

    private val exoPlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            coreVPlayerOnPlaybackStateChanged(playbackState)
        }
    }

    abstract fun coreVPlayerOnPlaybackStateChanged(playbackState: Int)
    abstract fun coreVPlayerOnAudioDeviceChange(
        audioDeviceInfo: AudioDeviceInfo,
        isBluetoothActive: Boolean
    )

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        handler = Handler(Looper.getMainLooper())
        exoPlayer = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
        exoPlayer.playWhenReady = true
        exoPlayer.addListener(exoPlayerListener)
    }

    private var audioDeviceInfo: AudioDeviceInfo? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkAudioOutputAboveM() {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        Log.d("MappLogger", "devicesLength ${devices.size}")
        for (i in devices.indices){
            val device = devices[i]
            Log.d("MappLogger", "deviceType $i ${device.type}")
            Log.d("MappLogger", "productName $i ${device.productName}")
            Log.d("MappLogger", "isBluetoothA2dpOn $i ${audioManager.isBluetoothA2dpOn}")
            Log.d("MappLogger", "isBluetoothScoOn $i ${audioManager.isBluetoothScoOn}")
            audioDeviceInfo = device
            coreVPlayerOnAudioDeviceChange(
                audioDeviceInfo!!,
                audioManager.isBluetoothA2dpOn || audioManager.isBluetoothScoOn
            )
        }
    }

    interface CVPlayerCallback {
        fun onDurationChange(duration: Long)
        fun onPositionChange(position: Long)

        fun onAudioOutputChange(audioDeviceInfo: AudioDeviceInfo, isBluetoothActive: Boolean)
    }
}