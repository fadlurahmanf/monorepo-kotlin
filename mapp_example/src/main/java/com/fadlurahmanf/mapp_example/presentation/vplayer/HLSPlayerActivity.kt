package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.media.AudioDeviceInfo
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlaybackException
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.utilities.HlsVideoPlayer
import com.fadlurahmanf.mapp_example.databinding.ActivityHlsPlayerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

@UnstableApi
class HLSPlayerActivity :
    BaseExampleActivity<ActivityHlsPlayerBinding>(ActivityHlsPlayerBinding::inflate),
    HlsVideoPlayer.HlsVPlayerCallback {
    private lateinit var hlsVideoPlayer: HlsVideoPlayer

    override fun injectActivity() {}

    override fun setup() {
        hlsVideoPlayer = HlsVideoPlayer(this)
        hlsVideoPlayer.initExoPlayer()
        hlsVideoPlayer.setCallback(this)

        binding.exoPlayer.player = hlsVideoPlayer.exoPlayer
        binding.exoPlayer.useController = false

        hlsVideoPlayer.playRemoteVideo("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
    }

    override fun onGetVideoQualities(list: List<QualityVideoModel>) {

    }

    override fun onVideoQualityChanged(quality: QualityVideoModel) {

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

    override fun onAudioOutputChange(audioDeviceInfo: AudioDeviceInfo, isBluetoothActive: Boolean) {

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