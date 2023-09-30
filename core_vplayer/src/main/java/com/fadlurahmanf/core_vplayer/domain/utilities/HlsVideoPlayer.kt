package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.video.VideoFrameMetadataListener
import kotlin.math.max

class HlsVideoPlayer(private val context: Context) : BaseVideoPlayer(context) {

    private lateinit var callback: HlsVPlayerCallback

    fun setCallback(callback: HlsVPlayerCallback) {
        this.callback = callback
    }

    /**
     * media source that contain multiple quality format, extension usually different from mp4
     */
    private fun mediaSourceFromHLS(url: String): HlsMediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                checkAudioOutputAboveM()
            }
            checkAudioPosition()
            handler.postDelayed(this, 1000)
        }
    }

    private var duration: Long? = null
    private var position: Long? = null
    private fun checkAudioPosition() {
        duration = exoPlayer.duration
        position = exoPlayer.currentPosition

        if (duration != null) {
            callback.onDurationChange(duration!!)
        }

        if (position != null) {
            callback.onPositionChange(position!!)
        }
    }

    private var isAlreadyInitialized: Boolean = false
    override fun coreVPlayerOnPlaybackStateChanged(playbackState: Int) {
        currentState = playbackState

        if (playbackState == Player.STATE_READY && !isAlreadyInitialized) {
            getVideoQualities()
            isAlreadyInitialized = true
        }
    }

    private val qualities: ArrayList<QualityVideoModel> = arrayListOf()

    private fun getVideoQualities() {
        val trackGroups = exoPlayer.currentTracks.groups.filter {
            it.type == C.TRACK_TYPE_VIDEO
        }

        if (trackGroups.isNotEmpty()) {
            val group = trackGroups.first()
            val mediaTrackGroup = group.mediaTrackGroup
            for (i in 0 until mediaTrackGroup.length) {
                val format = mediaTrackGroup.getFormat(i)
                val isExistInList = qualities.firstOrNull {
                    it.id == format.id
                } != null
                if (format.id != null && !isExistInList) {
                    val quality = convertFormatToVideoQuality(format)
                    if (quality != null) qualities.add(quality)
                }
            }
        }

        callback.onGetVideoQualities(qualities)
    }

    private var currentQuality: QualityVideoModel? = null
    private val videoFrameMetadataListener =
        VideoFrameMetadataListener { _, _, format, _ ->
            if (format.id != null) {
                val quality = convertFormatToVideoQuality(format)
                if (quality != null) {
                    if (currentQuality?.id != quality.id) {
                        callback.onVideoQualityChange(quality)
                        currentQuality = quality
                    }
                }
            }
        }

    private var deviceType: Int? = null

    @RequiresApi(Build.VERSION_CODES.S)
    private val communicationDevicesListener = AudioManager.OnCommunicationDeviceChangedListener {
        if (deviceType != it?.type) {
            Log.d("MappLogger", "CommunicationDevices: ${it?.type}")
            Log.d("MappLogger", "CommunicationDevices: ${it?.productName}")
            deviceType = it?.type
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAudioOutputAboveM() {
        Log.d(
            "MappLogger",
            "isBluetoothScoAvailableOffCall: ${audioManager.isBluetoothScoAvailableOffCall}"
        )
        Log.d("MappLogger", "isBluetoothScoOn: ${audioManager.isBluetoothScoOn}")
        Log.d("MappLogger", "isBluetoothA2dpOn: ${audioManager.isBluetoothA2dpOn}")
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        if (devices.isNotEmpty()) {
            val device = devices.first()
            if (deviceType != device?.type) {
                Log.d("MappLogger", "checkAudioOutputAboveM: ${device?.type}")
                Log.d("MappLogger", "checkAudioOutputAboveM: ${device?.productName}")
                deviceType = device?.type
            }
        }
    }

    fun playHlsRemoteAudio(url: String) {
        exoPlayer.setMediaSource(mediaSourceFromHLS(url))
        exoPlayer.setVideoFrameMetadataListener(videoFrameMetadataListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            audioManager.addOnCommunicationDeviceChangedListener(
                ContextCompat.getMainExecutor(
                    context
                ), communicationDevicesListener
            )
        }
        exoPlayer.prepare()
        handler.postDelayed(runnable, 1000)
    }

    private fun convertFormatToVideoQuality(format: Format): QualityVideoModel? {
        if (format.id == null) return null
        val max = max(format.height, format.width)
        return QualityVideoModel(
            id = format.id!!,
            formatName = "${max}p",
            formatPixel = max
        )
    }

    interface HlsVPlayerCallback : CVPlayerCallback {
        fun onGetVideoQualities(list: List<QualityVideoModel>)
        fun onVideoQualityChange(quality: QualityVideoModel)
    }

    fun destroy() {
        handler.removeCallbacks(runnable)
        exoPlayer.clearVideoFrameMetadataListener(videoFrameMetadataListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            audioManager.removeOnCommunicationDeviceChangedListener(communicationDevicesListener)
        }
        exoPlayer.stop()
        exoPlayer.release()
    }
}