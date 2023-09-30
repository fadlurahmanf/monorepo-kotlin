package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.util.Log
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.utilities.HlsVideoPlayer
import com.fadlurahmanf.mapp_example.databinding.ActivityVplayerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

class VPlayerActivity :
    BaseExampleActivity<ActivityVplayerBinding>(ActivityVplayerBinding::inflate),
    HlsVideoPlayer.HlsVPlayerCallback {
    private lateinit var videoPlayer: HlsVideoPlayer

    override fun injectActivity() {

    }

    override fun setup() {
        videoPlayer = HlsVideoPlayer(this)
        videoPlayer.setCallback(this)
        binding.exoPlayer.player = videoPlayer.exoPlayer
        binding.exoPlayer.useController = false
        videoPlayer.playHlsRemoteAudio("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
    }

    override fun onGetVideoQualities(list: List<QualityVideoModel>) {
        list.forEach {
            Log.d("MappLogger", "onGetVideoQualities: $it")
        }
    }

    override fun onVideoQualityChange(quality: QualityVideoModel) {
        Log.d("MappLogger", "onVideoQualityChange: $quality")
    }

    override fun onDurationChange(duration: Long) {
        Log.d("MappLogger", "onDurationChange: $duration")
    }

    override fun onPositionChange(position: Long) {
        Log.d("MappLogger", "onPositionChange: $position")
    }
}