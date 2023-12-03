package com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model

data class DefaultRecordingProperties(
    var name: String? = null,
    var hasAudio: Boolean? = null,
    var hasVideo: Boolean? = null,
    var outputMode: String? = null,
    var recordingLayout: String? = null,
    var resolution: String? = null,
    var frameRate: Int? = null,
    var shmSize: Long? = null,
    var mediaNode: String? = null
)
