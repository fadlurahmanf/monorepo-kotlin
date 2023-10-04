package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.media.AudioDeviceInfo
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlaybackException
import com.fadlurahmanf.core_vplayer.domain.utilities.Mp4VideoPlayer
import com.fadlurahmanf.mapp_example.databinding.ActivityMp4PlayerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

@UnstableApi
class Mp4PlayerPlayerActivity :
    BaseExampleActivity<ActivityMp4PlayerBinding>(ActivityMp4PlayerBinding::inflate),
    Mp4VideoPlayer.Mp4PlayerCallback {

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
        mp4VideoPlayer = Mp4VideoPlayer(this)
        mp4VideoPlayer.initExoPlayer()
        mp4VideoPlayer.setCallback(this)

        binding.playerView.player = mp4VideoPlayer.exoPlayer
        binding.playerView.useController = false

        playMp4RemoteVideo()
    }

    override fun onDestroy() {
        mp4VideoPlayer.destroyMp4Player()
        super.onDestroy()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        Log.d("MappLogger", "onPlaybackStateChanged: $playbackState")
    }

    override fun onDurationChanged(duration: Long) {
        Log.d("MappLogger", "onDurationChanged: $duration")
    }

    override fun onPositionChanged(position: Long) {
        Log.d("MappLogger", "onPositionChanged: $position")
    }

    override fun onAudioOutputChanged(audioDeviceInfo: AudioDeviceInfo) {

    }

    override fun onErrorHappened(exception: ExoPlaybackException) {
        Log.e("MappLogger", "onErrorHappened 1: ${exception.message}")
        Log.e("MappLogger", "onErrorHappened 2: ${exception.localizedMessage}")
        Log.e("MappLogger", "onErrorHappened 3: ${exception.cause?.message}")
        Log.e("MappLogger", "onErrorHappened 4: ${exception.cause?.localizedMessage}")
        Log.e("MappLogger", "onErrorHappened 5: ${exception.errorCode}")
        Log.e("MappLogger", "onErrorHappened 6: ${exception.errorCodeName}")
    }
}