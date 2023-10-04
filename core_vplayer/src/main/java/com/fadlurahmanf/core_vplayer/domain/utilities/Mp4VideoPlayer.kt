package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.util.EventLogger
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer2
import java.io.File

@UnstableApi
class Mp4VideoPlayer(private val context: Context) : BaseVideoPlayer2(context) {

    private fun createCacheDataSinkFactory(): CacheDataSink.Factory {
        return CacheDataSink.Factory()
            .setCache(CacheUtilities.getSimpleCache(context))
    }

    private fun createHttpDataSource(): DefaultDataSource.Factory {
        val dataSource = DefaultHttpDataSource.Factory()
        return DefaultDataSource.Factory(context, dataSource)
    }

    private fun createCacheDataSource(): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(CacheUtilities.getSimpleCache(context))
            .setCacheWriteDataSinkFactory(createCacheDataSinkFactory())
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setUpstreamDataSourceFactory(createHttpDataSource())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private fun createMediaSource(uriString: String): ProgressiveMediaSource {
        val mediaItem =
            MediaItem.fromUri(Uri.parse(uriString))
        return ProgressiveMediaSource.Factory(createCacheDataSource())
            .createMediaSource(mediaItem)
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

    interface Mp4Callback : BaseVideoPlayer2.CVPlayerCallback {
        fun onPlaybackStateChanged(playbackState: Int)
    }

}