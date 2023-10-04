package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.media.AudioDeviceInfo
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlaybackException
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.utilities.HlsVideoPlayer
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.databinding.ActivityHlsPlayerBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity

@UnstableApi
class HLSPlayerActivity :
    BaseExampleActivity<ActivityHlsPlayerBinding>(ActivityHlsPlayerBinding::inflate),
    HlsVideoPlayer.HlsVPlayerListener {
    private lateinit var videoPlayer: HlsVideoPlayer

    override fun injectActivity() {}

    override fun setup() {
        videoPlayer = HlsVideoPlayer(this)
        videoPlayer.setListener(this)
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
        binding.tvFormatType.text = "Auto(${quality.formatName})"
    }

    override fun onAudioOutputChange(audioDeviceInfo: AudioDeviceInfo, isBluetoothActive: Boolean) {
        if (isBluetoothActive) {
            binding.ivSpeakerType.background =
                ContextCompat.getDrawable(this, R.drawable.round_bluetooth_24)
        } else {
            binding.ivSpeakerType.background =
                ContextCompat.getDrawable(this, R.drawable.round_headset_24)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvSpeakerType.text = audioDeviceInfo.productName.toString()
        }
    }

    override fun onErrorHappened(exception: ExoPlaybackException) {
        Log.e("MappLogger", "onErrorHappened: ${exception.message}")
    }

    override fun onDurationChange(duration: Long) {
        Log.d("MappLogger", "onDurationChange: $duration")
    }

    override fun onPositionChange(position: Long) {
//        Log.d("MappLogger", "onPositionChange: $position")
    }

    override fun onDestroy() {
        videoPlayer.destroy()
        super.onDestroy()
    }
}