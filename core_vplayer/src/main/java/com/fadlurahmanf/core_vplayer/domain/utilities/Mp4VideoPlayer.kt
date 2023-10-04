package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer2

@UnstableApi
class Mp4VideoPlayer(context: Context) : BaseVideoPlayer2(context) {
    private fun createMediaSource(uriString: String): ProgressiveMediaSource {
        val dataSource = DefaultHttpDataSource.Factory()
        val mediaItem =
            MediaItem.fromUri(Uri.parse(uriString))
        return ProgressiveMediaSource.Factory(dataSource).createMediaSource(mediaItem)
    }

    private var mp4Callback: Mp4Callback? = null
    fun setCallback(mp4Callback: Mp4Callback) {
        this.mp4Callback = mp4Callback
    }

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            mp4Callback?.onPlaybackStateChanged(playbackState)

            if (exoPlayer.playerError != null) {
                mp4Callback?.onErrorHappened(exoPlayer.playerError!!)
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (mp4Callback != null) {
                fetchAudioDurationAndPosition(mp4Callback!!)
            }
            handler.postDelayed(this, 1000)
        }
    }

    fun playRemoteVideo(uriString: String) {
        exoPlayer.addListener(listener)
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

    interface Mp4Callback : BaseVideoPlayer2.CVPlayerCallback {
        fun onPlaybackStateChanged(playbackState: Int)
    }

}