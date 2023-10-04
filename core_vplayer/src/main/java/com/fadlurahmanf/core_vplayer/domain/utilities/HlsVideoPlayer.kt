package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.video.VideoFrameMetadataListener
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer2
import com.google.common.collect.Comparators.max

@UnstableApi
class HlsVideoPlayer(private val context: Context) : BaseVideoPlayer2(context) {

//    private lateinit var listener: HlsVPlayerListener
//
//    fun setListener(callback: HlsVPlayerListener) {
//        this.listener = callback
//    }
//
//    private val runnable = object : Runnable {
//        override fun run() {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                checkAudioOutputAboveM()
//            }
//            checkAudioPosition()
//            handler.postDelayed(this, 1000)
//        }
//    }
//
//    private var duration: Long? = null
//    private var position: Long? = null
//    private fun checkAudioPosition() {
//        if (duration == null || exoPlayer.duration != duration) {
//            duration = exoPlayer.duration
//            listener.onDurationChange(duration!!)
//        }
//
//        if (position == null || exoPlayer.currentPosition != position) {
//            position = exoPlayer.currentPosition
//            listener.onPositionChange(position!!)
//        }
//    }
//
//    private val qualities: ArrayList<QualityVideoModel> = arrayListOf()
//
//    private fun getVideoQualities() {
//        val trackGroups = exoPlayer.currentTracks.groups.filter {
//            it.type == C.TRACK_TYPE_VIDEO
//        }
//
//        if (trackGroups.isNotEmpty()) {
//            val group = trackGroups.first()
//            val mediaTrackGroup = group.mediaTrackGroup
//            for (i in 0 until mediaTrackGroup.length) {
//                val format = mediaTrackGroup.getFormat(i)
//                val isExistInList = qualities.firstOrNull {
//                    it.id == format.id
//                } != null
//                if (format.id != null && !isExistInList) {
//                    val quality = convertFormatToVideoQuality(format)
//                    if (quality != null) qualities.add(quality)
//                }
//            }
//        }
//
//        listener.onGetVideoQualities(qualities)
//    }
//
//    private var latestErrorPlayerErrorCode: Int? = null
//
//    private var isAlreadyFetchVideoQuality: Boolean = false
//    private val exoPlayerListener = object : Player.Listener {
//        override fun onPlaybackStateChanged(playbackState: Int) {
//            super.onPlaybackStateChanged(playbackState)
//
//            if (!isAlreadyFetchVideoQuality && playbackState == Player.STATE_READY) {
//                getVideoQualities()
//                isAlreadyFetchVideoQuality = true
//            }
//
//            if (exoPlayer.playerError != null && exoPlayer.playerError?.errorCode != latestErrorPlayerErrorCode) {
//                latestErrorPlayerErrorCode = exoPlayer.playerError?.errorCode
//                listener.onErrorHappened(exoPlayer.playerError!!)
//            }
//
//            Log.d("MappLogger", "MASUK ERROR 1: ${exoPlayer.playerError?.message}")
//            Log.d("MappLogger", "MASUK ERROR 2: ${exoPlayer.playerError?.localizedMessage}")
//            Log.d("MappLogger", "MASUK ERROR 3: ${exoPlayer.playerError?.cause?.message}")
//            Log.d("MappLogger", "MASUK ERROR 4: ${exoPlayer.playerError?.cause?.localizedMessage}")
//            Log.d("MappLogger", "MASUK ERROR 5: ${exoPlayer.playerError?.errorCodeName}")
//            Log.d("MappLogger", "MASUK ERROR 6: ${exoPlayer.playerError?.errorCode}")
//        }
//    }
//
//    fun playHlsRemoteAudio(url: String) {
//        exoPlayer.setMediaSource(mediaSourceFromHLS(url))
//        exoPlayer.setVideoFrameMetadataListener(videoFrameMetadataListener)
//        exoPlayer.addListener(exoPlayerListener)
//        exoPlayer.prepare()
//        handler.postDelayed(runnable, 1000)
//    }
//
//    fun destroy() {
//        handler.removeCallbacks(runnable)
//        exoPlayer.clearVideoFrameMetadataListener(videoFrameMetadataListener)
//        exoPlayer.removeListener(exoPlayerListener)
//        exoPlayer.stop()
//        exoPlayer.release()
//    }

    /**
     * media source that contain multiple quality format, extension usually different from mp4
     */
    private fun createMediaSourceHLS(uriString: String): HlsMediaSource {
        val mediaItem = MediaItem.fromUri(Uri.parse(uriString))
        return HlsMediaSource.Factory(createHttpDataSource()).createMediaSource(mediaItem)
    }

    private var hlsVPlayerCallback: HlsVPlayerCallback? = null
    fun setCallback(callback: HlsVPlayerCallback) {
        this.hlsVPlayerCallback = callback
    }

    private var currentQuality: QualityVideoModel? = null
    private val videoFrameMetadataListener =
        VideoFrameMetadataListener { _, _, format, _ ->
            if (format.id != null) {
                val quality = convertFormatToVideoQuality(format)
                if (quality != null) {
                    if (currentQuality?.id != quality.id) {
                        hlsVPlayerCallback?.onVideoQualityChanged(quality)
                        currentQuality = quality
                    }
                }
            }
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

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            hlsVPlayerCallback?.onPlaybackStateChanged(playbackState)

            if (exoPlayer.playerError != null) {
                hlsVPlayerCallback?.onErrorHappened(exoPlayer.playerError!!)
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (hlsVPlayerCallback != null) {
                fetchAudioDurationAndPosition(hlsVPlayerCallback!!)
            }
            handler.postDelayed(this, 1000)
        }
    }

    fun playRemoteVideo(uriString: String) {
        exoPlayer.setVideoFrameMetadataListener(videoFrameMetadataListener)
        exoPlayer.addListener(listener)
        exoPlayer.setMediaSource(createMediaSourceHLS(uriString))
        exoPlayer.prepare()
        handler.post(runnable)
    }

    fun destroyHlsPlayer() {
        handler.removeCallbacks(runnable)
        exoPlayer.clearVideoFrameMetadataListener(videoFrameMetadataListener)
        exoPlayer.removeListener(listener)
        exoPlayer.stop()
        exoPlayer.release()
    }

    interface HlsVPlayerCallback : CVPlayerCallback {
        fun onGetVideoQualities(list: List<QualityVideoModel>)
        fun onVideoQualityChanged(quality: QualityVideoModel)
    }
}