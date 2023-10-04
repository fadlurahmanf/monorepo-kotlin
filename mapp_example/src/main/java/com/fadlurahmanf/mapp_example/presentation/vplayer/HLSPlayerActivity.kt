package com.fadlurahmanf.mapp_example.presentation.vplayer

import android.app.AlertDialog
import android.media.AudioDeviceInfo
import android.os.Build
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


        binding.ll1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val array = arrayOfNulls<String>(qualities.size)
            var checkedItem: Int = 0
            for (i in qualities.indices) {
                if (qualities[i].id == currentQuality?.id) {
                    checkedItem = i
                }
                array[i] = qualities[i].formatName ?: "-"
            }
            builder.setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("APPLY") { dialog, _ ->
                hlsVideoPlayer.selectQualityOfVideo(qualities[checkedItem].id)
                dialog.dismiss()
            }
            builder.setSingleChoiceItems(
                array, checkedItem
            ) { _, which ->
                checkedItem = which
            }

            builder.create().show()
        }
    }

    private val qualities: ArrayList<QualityVideoModel> = arrayListOf()
    override fun onGetVideoQualities(list: List<QualityVideoModel>) {
        list.forEach {
            Log.d("MappLogger", "onGetVideoQuality: $it")
        }

        qualities.clear()
        qualities.addAll(list)
    }

    private var currentQuality: QualityVideoModel? = null
    override fun onVideoQualityChanged(quality: QualityVideoModel, isAutoQuality: Boolean) {
        Log.d("MappLogger", "onVideoQualityChanged: $quality")
        currentQuality = quality
        runOnUiThread {
            binding.tvFormatType.text = if (isAutoQuality) "Auto(${quality.formatName})" else "${quality.formatName}"
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("MappLogger", "onAudioOutputChange: audioDeviceId: ${audioDeviceInfo.id}")
            Log.d(
                "MappLogger",
                "onAudioOutputChange: audioDeviceName: ${audioDeviceInfo.productName}"
            )
            Log.d("MappLogger", "onAudioOutputChange: audioDeviceType: ${audioDeviceInfo.type}")
            runOnUiThread {
                binding.tvSpeakerType.text = audioDeviceInfo.productName
            }
        }
    }

    override fun onErrorHappened(exception: ExoPlaybackException) {
        Log.e("MappLogger", "onErrorHappened 1: ${exception.message}")
        Log.e("MappLogger", "onErrorHappened 2: ${exception.localizedMessage}")
        Log.e("MappLogger", "onErrorHappened 3: ${exception.cause?.message}")
        Log.e("MappLogger", "onErrorHappened 4: ${exception.cause?.localizedMessage}")
        Log.e("MappLogger", "onErrorHappened 5: ${exception.errorCode}")
        Log.e("MappLogger", "onErrorHappened 6: ${exception.errorCodeName}")
    }

    override fun onDestroy() {
        hlsVideoPlayer.destroyHlsPlayer()
        super.onDestroy()
    }
}