package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.media.AudioDeviceInfo
import android.net.Uri
import android.os.Build
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.video.VideoFrameMetadataListener
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer
import kotlin.math.max

@UnstableApi
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
            println("MASUK SINI ${exoPlayer.playbackState}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    fun playHlsRemoteAudio(url: String) {
        exoPlayer = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
        exoPlayer.setMediaSource(mediaSourceFromHLS(url))
        exoPlayer.setVideoFrameMetadataListener(videoFrameMetadataListener)
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
        handler.postDelayed(runnable, 1000)
    }

    override fun coreVPlayerOnAudioDeviceChange(
        audioDeviceInfo: AudioDeviceInfo,
        isBluetoothActive: Boolean
    ) {
        callback.onAudioOutputChange(audioDeviceInfo, isBluetoothActive)
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
        exoPlayer.stop()
        exoPlayer.release()
    }
}