package com.fadlurahmanf.core_vplayer.domain.common

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector

@UnstableApi
abstract class BaseVideoPlayer2(private val context: Context) {
    interface CVPlayerCallback {
        fun onDurationChange(duration: Long)
        fun onPositionChange(position: Long)

        fun onAudioOutputChange(audioDeviceInfo: AudioDeviceInfo, isBluetoothActive: Boolean)
        fun onErrorHappened(exception: ExoPlaybackException)
    }
}

@UnstableApi
abstract class BaseVideoPlayer(private val context: Context) {

    private var audioManager: AudioManager
    var handler: Handler
    lateinit var exoPlayer: ExoPlayer
    private val trackSelector: TrackSelector =
        DefaultTrackSelector(context, AdaptiveTrackSelection.Factory())

//    abstract fun coreVPlayerOnPlaybackStateChanged(playbackState: Int)
//    abstract fun coreVPlayerOnAudioDeviceChange(
//        audioDeviceInfo: AudioDeviceInfo,
//        isBluetoothActive: Boolean
//    )

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        handler = Handler(Looper.getMainLooper())
        exoPlayer = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
        exoPlayer.playWhenReady = true
    }

    private var audioDeviceInfo: AudioDeviceInfo? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkAudioOutputAboveM() {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        for (i in devices.indices) {
            val device = devices[i]
            audioDeviceInfo = device
        }
    }

    interface CVPlayerCallback {
        fun onDurationChange(duration: Long)
        fun onPositionChange(position: Long)

        fun onAudioOutputChange(audioDeviceInfo: AudioDeviceInfo, isBluetoothActive: Boolean)
        fun onErrorHappened(exception: ExoPlaybackException)
    }
}