package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.util.EventLogger
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer2

@UnstableApi
class Mp4VideoPlayer(private val context: Context) : BaseVideoPlayer2(context) {

    private fun createMediaSource(uriString: String): ProgressiveMediaSource {
        val mediaItem =
            MediaItem.fromUri(Uri.parse(uriString))
        return ProgressiveMediaSource.Factory(createCacheDataSource())
            .createMediaSource(mediaItem)
    }

    private var mp4PlayerCallback: Mp4PlayerCallback? = null
    fun setCallback(mp4PlayerCallback: Mp4PlayerCallback) {
        this.mp4PlayerCallback = mp4PlayerCallback
    }

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            mp4PlayerCallback?.onPlaybackStateChanged(playbackState)

            if (exoPlayer.playerError != null) {
                mp4PlayerCallback?.onErrorHappened(exoPlayer.playerError!!)
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (mp4PlayerCallback != null) {
                fetchAudioDurationAndPosition(mp4PlayerCallback!!)
            }
            handler.postDelayed(this, 1000)
        }
    }

    fun playRemoteVideo(uriString: String) {
        exoPlayer.addListener(listener)
        exoPlayer.addAnalyticsListener(EventLogger())
        exoPlayer.setMediaSource(createMediaSource(uriString))
        exoPlayer.prepare()
        handler.post(runnable)
    }

    fun destroyMp4Player() {
        handler.removeCallbacks(runnable)
        exoPlayer.removeListener(listener)
        exoPlayer.stop()
        exoPlayer.release()
    }

    interface Mp4PlayerCallback : BaseVideoPlayer2.CVPlayerCallback {
    }

}