package com.fadlurahmanf.core_vplayer.domain.common

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
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

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        handler = Handler(Looper.getMainLooper())
        exoPlayer = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
        exoPlayer.playWhenReady = true
        exoPlayer.addListener(exoPlayerListener)
    }

    interface CVPlayerCallback {
        fun onDurationChange(duration: Long)
        fun onPositionChange(position: Long)
    }
}