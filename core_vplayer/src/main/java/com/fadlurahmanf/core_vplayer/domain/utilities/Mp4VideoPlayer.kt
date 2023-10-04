package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer2

@UnstableApi
class Mp4VideoPlayer(private val context: Context) : BaseVideoPlayer2(context) {
    lateinit var exoPlayer: ExoPlayer

    private fun createMediaSource(uriString: String): ProgressiveMediaSource {
        val dataSource = DefaultHttpDataSource.Factory()
        val mediaItem =
            MediaItem.fromUri(Uri.parse(uriString))
        return ProgressiveMediaSource.Factory(dataSource).createMediaSource(mediaItem)
    }

    fun initExoPlayer() {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.playWhenReady = true
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

    fun playRemoteVideo(uriString: String) {
        exoPlayer.addListener(listener)
        exoPlayer.setMediaSource(createMediaSource(uriString))
        exoPlayer.prepare()
    }

    fun destroyMp4Player() {
        exoPlayer.removeListener(listener)
        exoPlayer.stop()
        exoPlayer.release()
    }

    interface Mp4Callback : BaseVideoPlayer2.CVPlayerCallback {
        fun onPlaybackStateChanged(playbackState: Int)
    }

}