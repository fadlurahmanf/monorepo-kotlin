package com.fadlurahmanf.core_vplayer.domain.utilities

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector
import androidx.media3.exoplayer.video.VideoFrameMetadataListener
import com.fadlurahmanf.core_vplayer.data.model.QualityVideoModel
import com.fadlurahmanf.core_vplayer.domain.common.BaseVideoPlayer2
import com.google.common.collect.Comparators.max

@UnstableApi
class HlsVideoPlayer(private val context: Context) : BaseVideoPlayer2(context) {

    private lateinit var trackSelector: TrackSelector

    override fun getExoPlayerBuilder(): ExoPlayer.Builder {
        trackSelector = DefaultTrackSelector(context, AdaptiveTrackSelection.Factory())
        return ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
    }

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
                        hlsVPlayerCallback?.onVideoQualityChanged(quality, isAutoQuality)
                        currentQuality = quality
                    }
                }
            }
        }

    private var isAutoQuality: Boolean = true
    fun selectQualityOfVideo(formatId: String) {
        var index: Int? = null
        for (i in 0 until mediaTrackGroup.length) {
            val format = mediaTrackGroup.getFormat(i)
            if (format.id == formatId) {
                index = i
                break
            }
        }
        if (index != null) {
            isAutoQuality = false
            trackSelector.parameters = trackSelector.parameters.buildUpon()
                .addOverride(TrackSelectionOverride(mediaTrackGroup, index))
                .build()
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

            if (!isAlreadyFetchVideoQualities || qualities.isEmpty()) {
                fetchVideoQualities()
            }
        }
    }

    private var isAlreadyFetchVideoQualities: Boolean = false
    private val qualities: ArrayList<QualityVideoModel> = arrayListOf()
    private lateinit var mediaTrackGroup: TrackGroup
    private fun fetchVideoQualities() {
        val trackGroups = exoPlayer.currentTracks.groups.filter {
            it.type == C.TRACK_TYPE_VIDEO
        }

        if (trackGroups.isNotEmpty()) {
            val group = trackGroups.first()
            mediaTrackGroup = group.mediaTrackGroup
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

        hlsVPlayerCallback?.onGetVideoQualities(qualities)
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (hlsVPlayerCallback != null) {
                fetchAudioDurationAndPosition(hlsVPlayerCallback!!)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkAudioOutputAboveM(hlsVPlayerCallback!!)
                }
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
        fun onVideoQualityChanged(quality: QualityVideoModel, isAutoQuality: Boolean)
    }
}