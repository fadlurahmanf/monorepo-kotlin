package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.databinding.ActivityMp4PlayerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

@UnstableApi
class Mp4PlayerActivity : BaseExampleActivity<ActivityMp4PlayerBinding>(ActivityMp4PlayerBinding::inflate) {

    override fun injectActivity() {

    }

    private lateinit var exoPlayer:ExoPlayer
    override fun setup() {
        setupExoPlayer()
        playMp4RemoteVideo()
    }

    private fun playMp4RemoteVideo() {
        val dataSource = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        val mediaSource = ProgressiveMediaSource.Factory(dataSource).createMediaSource(mediaItem)
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    private fun setupExoPlayer(){
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.playWhenReady = true

        binding.playerView.useController = false
        binding.playerView.player = exoPlayer

    }

    override fun onDestroy() {
        exoPlayer.stop()
        exoPlayer.release()
        super.onDestroy()
    }
}