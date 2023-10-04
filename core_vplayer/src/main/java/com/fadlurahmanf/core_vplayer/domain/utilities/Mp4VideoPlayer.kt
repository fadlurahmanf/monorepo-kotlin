package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource

@UnstableApi
class Mp4VideoPlayer {
    lateinit var exoPlayer: ExoPlayer


    fun createMediaSource(uriString: String): ProgressiveMediaSource {
        val dataSource = DefaultHttpDataSource.Factory()
        val mediaItem =
            MediaItem.fromUri(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        return ProgressiveMediaSource.Factory(dataSource).createMediaSource(mediaItem)
    }

    fun initExoPlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.playWhenReady = true
    }

    fun playRemoteVideo(uriString: String) {
        exoPlayer.setMediaSource(createMediaSource(uriString))
        exoPlayer.prepare()
    }

    fun destroyMp4Player() {
        exoPlayer.stop()
        exoPlayer.release()
    }

}