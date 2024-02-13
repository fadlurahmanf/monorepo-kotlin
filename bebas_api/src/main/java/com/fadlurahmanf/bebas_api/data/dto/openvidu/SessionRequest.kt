package com.fadlurahmanf.bebas_api.data.dto.openvidu

data class SessionRequest(
    var mediaMode: String? = "ROUTED",
    var recordingMode: String? = "MANUAL",
    val customSessionId: String,
    var forcedVideoCodec: String? = null,
    var allowTranscoding: Boolean? = null,
)
