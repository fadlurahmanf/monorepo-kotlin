package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.fadlurahmanf.core_vplayer.domain.utilities.Mp4VideoPlayer
import com.fadlurahmanf.mapp_example.databinding.ActivityMp4PlayerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

@UnstableApi
class Mp4PlayerActivity :
    BaseExampleActivity<ActivityMp4PlayerBinding>(ActivityMp4PlayerBinding::inflate) {

    override fun injectActivity() {

    }
    override fun setup() {
        initExoPlayer()
    }

    private fun playMp4RemoteVideo() {
        mp4VideoPlayer.playRemoteVideo("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    }

    private lateinit var mp4VideoPlayer: Mp4VideoPlayer
    private fun initExoPlayer() {
        mp4VideoPlayer = Mp4VideoPlayer()
        mp4VideoPlayer.initExoPlayer(this)

        binding.playerView.player = mp4VideoPlayer.exoPlayer
        binding.playerView.useController = false

        playMp4RemoteVideo()
    }

    override fun onDestroy() {
        mp4VideoPlayer.destroyMp4Player()
        super.onDestroy()
    }
}