package com.fadlurahmanf.core_vplayer.domain.common

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector
import com.fadlurahmanf.core_vplayer.domain.utilities.CacheUtilities

@UnstableApi
abstract class BaseVideoPlayer2(private val context: Context) {
    val handler = Handler(Looper.getMainLooper())
    lateinit var exoPlayer: ExoPlayer

    open fun getExoPlayerBuilder(): ExoPlayer.Builder {
        return ExoPlayer.Builder(context)
    }

    open fun initExoPlayer() {
        exoPlayer = getExoPlayerBuilder().build()
        exoPlayer.playWhenReady = true
    }


    private var currentDuration: Long? = null
    private var currentPosition: Long? = null
    open fun fetchAudioDurationAndPosition(callback: CVPlayerCallback) {
        if (currentDuration == null || (currentDuration
                ?: 0L) <= 0L || exoPlayer.duration != currentDuration
        ) {
            currentDuration = exoPlayer.duration
            callback.onDurationChanged(currentDuration!!)
        }

        if (currentPosition == null || exoPlayer.currentPosition != currentPosition) {
            currentPosition = exoPlayer.currentPosition
            callback.onPositionChanged(currentPosition!!)
        }
    }

    open fun createCacheDataSinkFactory(): CacheDataSink.Factory {
        return CacheDataSink.Factory()
            .setCache(CacheUtilities.getSimpleCache(context))
    }

    open fun createHttpDataSource(): DefaultDataSource.Factory {
        val dataSource = DefaultHttpDataSource.Factory()
        return DefaultDataSource.Factory(context, dataSource)
    }

    open fun createCacheDataSource(): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(CacheUtilities.getSimpleCache(context))
            .setCacheWriteDataSinkFactory(createCacheDataSinkFactory())
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setUpstreamDataSourceFactory(createHttpDataSource())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    interface CVPlayerCallback {
        fun onPlaybackStateChanged(playbackState: Int)
        fun onDurationChanged(duration: Long)
        fun onPositionChanged(position: Long)

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